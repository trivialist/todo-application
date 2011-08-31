/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.tablemodel;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import todo.util.GlobalDatabaseSearch.SearchHit;

public class SearchResultTableModel extends AbstractTableModel
{
	private ArrayList<SearchHit> searchResults = null;
	private String[] columnNames = {"Relevanz", "Such-Treffer", "Element-Typ"};
	private HashMap<String, String> assignedDescriptions = new HashMap<String, String>();
	private String searchPhrase = null;
	private float variableFactor = 1f;

	public SearchResultTableModel(ArrayList<SearchHit> searchResults, String searchPhrase)
	{
		this.searchResults = searchResults;
		this.searchPhrase = searchPhrase;

		assignedDescriptions.put("todo.entity.Area", "Bereich");
		assignedDescriptions.put("todo.entity.Institution", "Institution");
		assignedDescriptions.put("todo.entity.Category", "Kategorie");
		assignedDescriptions.put("todo.entity.Memo", "Memo");
		assignedDescriptions.put("todo.entity.Todo", "Protokollelement");
		assignedDescriptions.put("todo.entity.MeetingType", "Sitzungsart");
		assignedDescriptions.put("todo.entity.Meeting", "Sitzung");
		assignedDescriptions.put("todo.entity.FinStatus", "Status");
		assignedDescriptions.put("todo.entity.Topic", "Thema");

		int highestRelevance = 0;
		for (int i = 0; i < getRowCount(); i++)
		{
			if(highestRelevance < (Integer)getValueAt(i, 0))
			{
				highestRelevance = (Integer)getValueAt(i, 0);
			}
		}

		variableFactor = 100f / highestRelevance;
	}

	@Override
	public int getRowCount()
	{
		return searchResults.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch(columnIndex)
		{
			case -2:
				return searchResults.get(rowIndex).itemType;
			case -1:
				return searchResults.get(rowIndex).itemId;
			case 0:
				return (int)((float)searchResults.get(rowIndex).matchingTexts.size() * (float)searchResults.get(rowIndex).rankingFactor * variableFactor);
			case 1:
				return reformatHits(searchResults.get(rowIndex).matchingTexts);			
			case 2:
				return assignedDescriptions.get(searchResults.get(rowIndex).itemType.getName());
		}

		return null;
	}

	private String reformatHits(HashMap<String, String> searchHits)
	{
		StringBuilder reformattedHits = new StringBuilder();

		for(String columnName : searchHits.keySet())
		{
			reformattedHits.append(columnName);
			reformattedHits.append(" -> ");
			reformattedHits.append(searchHits.get(columnName).replaceAll("[\r\n]", " ").replaceAll("[ \t]+", " ").replaceAll("<(.*?)>", ""));
			reformattedHits.append("\n");
		}

		return reformattedHits.toString();
	}

	public int getRowHeight(int rowIndex)
	{
		return searchResults.get(rowIndex).matchingTexts.size() * 16;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex == 0)
		{
			return Integer.class;
		}

		return super.getColumnClass(columnIndex);
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}
}
