/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.gui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import todo.core.Area;
import todo.core.Category;
import todo.core.FinStatus;
import todo.core.Institution;
import todo.core.Meeting;
import todo.core.MeetingType;
import todo.core.Memo;
import todo.core.Todo;
import todo.core.Topic;

/**
 *
 * @author Sven
 */
public class GlobalDatabaseSearch
{
	private HashMap<String, TableDescription> textColumns = new HashMap<String, TableDescription>();
	private ArrayList<SearchHit> matchingSearchHits = new ArrayList<SearchHit>();
	private String searchPhrase = "";
	private Connection databaseConnection = null;
	private final int numberOfTheads = 5;
	private final Semaphore threadSemaphore = new Semaphore(numberOfTheads);

	class TableDescription
	{
		public String primaryKeyColumn = "";
		public ArrayList<String> textColumns = new ArrayList<String>();
		public Class itemType = null;
		public int rankingFactor = 0;
	}

	public class SearchHit
	{
		public HashMap<String, String> matchingTexts = new HashMap<String, String>();
		public Class itemType = null;
		public int itemId = 0;
		public int rankingFactor = 1;
	}

	public GlobalDatabaseSearch()
	{
		TableDescription tmpItem = new TableDescription();
		tmpItem.textColumns.add("Name");
		tmpItem.textColumns.add("Beschreibung");
		tmpItem.primaryKeyColumn = "BereichID";
		tmpItem.itemType = Area.class;
		tmpItem.rankingFactor = 1;
		textColumns.put("Bereich", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Name");
		tmpItem.primaryKeyColumn = "InstitutionID";
		tmpItem.itemType = Institution.class;
		tmpItem.rankingFactor = 1;
		textColumns.put("Institution", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Name");
		tmpItem.textColumns.add("Beschreibung");
		tmpItem.primaryKeyColumn = "KategorieID";
		tmpItem.itemType = Category.class;
		tmpItem.rankingFactor = 1;
		textColumns.put("Kategorie", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Inhalt");
		tmpItem.textColumns.add("Benutzer");
		tmpItem.primaryKeyColumn = "MemoID";
		tmpItem.itemType = Memo.class;
		tmpItem.rankingFactor = 4;
		textColumns.put("Memo", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Thema");
		tmpItem.textColumns.add("Inhalt");
		tmpItem.textColumns.add("Überschrift");
		tmpItem.textColumns.add("Kopiergrund");
		tmpItem.primaryKeyColumn = "ToDoID";
		tmpItem.itemType = Todo.class;
		tmpItem.rankingFactor = 12;
		textColumns.put("Protokollelement", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Name");
		tmpItem.primaryKeyColumn = "SitzungsartID";
		tmpItem.itemType = MeetingType.class;
		tmpItem.rankingFactor = 1;
		textColumns.put("Sitzungsart", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Ort");
		tmpItem.textColumns.add("Tagesordnung");
		tmpItem.textColumns.add("Sonstige");
		tmpItem.primaryKeyColumn = "SitzungsdatenID";
		tmpItem.itemType = Meeting.class;
		tmpItem.rankingFactor = 6;
		textColumns.put("Sitzungsdaten", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Name");
		tmpItem.primaryKeyColumn = "StatusID";
		tmpItem.itemType = FinStatus.class;
		tmpItem.rankingFactor = 1;
		textColumns.put("Status", tmpItem);

		tmpItem = new TableDescription();
		tmpItem.textColumns.add("Name");
		tmpItem.textColumns.add("Beschreibung");
		tmpItem.primaryKeyColumn = "ThemaID";
		tmpItem.itemType = Topic.class;
		tmpItem.rankingFactor = 1;
		textColumns.put("Thema", tmpItem);
	}

	public ArrayList<SearchHit> doSearch(Connection databaseConnection, String searchPhrase)
	{
		this.searchPhrase = searchPhrase.toLowerCase();
		this.databaseConnection = databaseConnection;

		for (final String tableName : textColumns.keySet())
		{
			try
			{
				threadSemaphore.acquire();
			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(GlobalDatabaseSearch.class.getName()).log(Level.SEVERE, null, ex);
			}

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					doSingleSearch(tableName);
				}
			}).start();
		}

		//wait for search to finish
		while(threadSemaphore.availablePermits() < numberOfTheads)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(GlobalDatabaseSearch.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return matchingSearchHits;
	}

	private synchronized void doSingleSearch(String tableName)
	{
		try
		{
			String searchColumns = convertArrayListToString(textColumns.get(tableName).textColumns);
			Statement currentStatement = databaseConnection.createStatement();
			String sqlClause = "SELECT " + textColumns.get(tableName).primaryKeyColumn + ", " + searchColumns + " FROM " + tableName;

			ResultSet queryResults = currentStatement.executeQuery(sqlClause);
			while(queryResults.next())
			{
				int itemId = queryResults.getInt(textColumns.get(tableName).primaryKeyColumn);
				SearchHit newHit = null;

				for(String currentColumn : textColumns.get(tableName).textColumns)
				{
					String columnTextContent = queryResults.getString(currentColumn);

					//skip text fields that are empty or set to null
					if(columnTextContent == null)continue;

					if(columnTextContent.toLowerCase().contains(searchPhrase))
					{
						if(newHit == null)
						{
							newHit = new SearchHit();
							newHit.itemId = itemId;
							newHit.itemType = textColumns.get(tableName).itemType;
							newHit.rankingFactor = textColumns.get(tableName).rankingFactor;
						}

						newHit.matchingTexts.put(currentColumn, columnTextContent);
					}
				}

				if(newHit != null)
				{
					matchingSearchHits.add(newHit);
				}
			}

			queryResults.close();
			currentStatement.close();
		}
		catch (SQLException ex)
		{
			Logger.getLogger(GlobalDatabaseSearch.class.getName()).log(Level.SEVERE, null, ex);
		}

		threadSemaphore.release();
	}

	private static String convertArrayListToString(ArrayList<String> listToConvert)
	{
		String listString = "";

		for (String s : listToConvert)
		{
			listString += s + ", ";
		}

		return (listString.length() >= 2) ? listString.substring(0, listString.length() - 2) : "";
	}
}
