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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import todo.entity.Meeting;
import todo.entity.Todo;
import todo.db.DatabaseEmployeeConnect;
import todo.db.DatabaseTodoConnect;
import todo.util.DataReloadable;

public class PersonalItemTableModel extends AbstractTableModel implements DataReloadable
{
	private static final String columnNames[] = {"Typ", "Zusammenfassung"};
	private int personalizeUsernameId;
	private ArrayList<PersonalItem> personalizedItemList = new ArrayList<PersonalItem>();

	class PersonalItem
	{
		public Class itemType = null;
		public String itemSummary = null;
		public int itemId = 0;
	}

	public PersonalItemTableModel(int personalizeUsernameId)
	{
		this.personalizeUsernameId = personalizeUsernameId;
		
		if(personalizeUsernameId != 0)
		{
			//load list for currently logged in user
			loadPersonalizedData(personalizeUsernameId);
		}
	}
	
	public void reloadData()
	{
		personalizedItemList.clear();
		
		if(personalizeUsernameId != 0)
		{
			//load list for currently logged in user
			loadPersonalizedData(personalizeUsernameId);
		}
	}

	private void addResponsibleItems(int personnelId)
	{
		try
		{
			Connection dbConnection = DatabaseTodoConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT * FROM Protokollelement INNER JOIN todo_responsible_personnel ON Protokollelement.ToDoID = todo_responsible_personnel.todoID WHERE Geloescht = false AND StatusID <> 1 AND todo_responsible_personnel.personnelID = " + personnelId + " ORDER BY Protokollelement.ToDoID DESC");

			while(dbResult.next())
			{
				PersonalItem newItem = new PersonalItem();

				newItem.itemId = dbResult.getInt("ToDoID");
				newItem.itemType = Todo.class;
				newItem.itemSummary = "<html><b>Verantwortlich: </b>" + dbResult.getString("Überschrift") + "</html>";

				personalizedItemList.add(newItem);
			}

			dbResult.close();
			dbStatement.close();
			DatabaseTodoConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addInvolvedItems(int personnelId)
	{
		try
		{
			Connection dbConnection = DatabaseTodoConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT * FROM Protokollelement INNER JOIN todo_involved_personnel ON Protokollelement.ToDoID = todo_involved_personnel.todoID WHERE Geloescht = false AND StatusID <> 1 AND todo_involved_personnel.personnelID = " + personnelId + " ORDER BY Protokollelement.ToDoID DESC");

			while(dbResult.next())
			{
				PersonalItem newItem = new PersonalItem();

				newItem.itemId = dbResult.getInt("ToDoID");
				newItem.itemType = Todo.class;
				newItem.itemSummary = "<html><b>Beteiligt: </b>" + dbResult.getString("Überschrift") + "</html>";

				personalizedItemList.add(newItem);
			}

			dbResult.close();
			dbStatement.close();
			DatabaseTodoConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addAttendeeItems(int personnelId)
	{
		try
		{
			Connection dbConnection = DatabaseTodoConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT * FROM Sitzungsdaten INNER JOIN meeting_attendee_personnel ON Sitzungsdaten.SitzungsdatenID = meeting_attendee_personnel.meetingID WHERE Geloescht = false AND meeting_attendee_personnel.personnelID = " + personnelId + " ORDER BY Datum DESC");

			while(dbResult.next())
			{
				PersonalItem newItem = new PersonalItem();

				newItem.itemId = dbResult.getInt("SitzungsdatenID");
				newItem.itemType = Meeting.class;
				newItem.itemSummary = "<html><b>Teilnehmer: </b>" + dbResult.getString("Ort") + "</html>";

				personalizedItemList.add(newItem);
			}

			dbResult.close();
			dbStatement.close();
			DatabaseTodoConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addReminderItems()
	{
		try
		{
			Connection dbConnection = DatabaseTodoConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT * FROM Protokollelement WHERE WiedervorlageGesetzt = true AND WiedervorlageDatum >= NOW() AND StatusID <> 1 AND Geloescht = false ORDER BY ToDoID DESC");

			while(dbResult.next())
			{
				PersonalItem newItem = new PersonalItem();

				newItem.itemId = dbResult.getInt("ToDoID");
				newItem.itemType = Todo.class;
				newItem.itemSummary = "<html><b>Wiedervorlage: </b>" + dbResult.getString("Überschrift") + "</html>";

				personalizedItemList.add(newItem);
			}

			dbResult.close();
			dbStatement.close();
			DatabaseTodoConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addRemainingItems()
	{
		try
		{
			Connection dbConnection = DatabaseTodoConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT * FROM Sitzungsdaten WHERE Geloescht = false ORDER BY Datum DESC");

			while(dbResult.next())
			{
				PersonalItem newItem = new PersonalItem();

				newItem.itemId = dbResult.getInt("SitzungsdatenID");
				newItem.itemType = Meeting.class;
				newItem.itemSummary = "<html><b>Sitzung: </b>" + dbResult.getString("Ort") + "</html>";

				personalizedItemList.add(newItem);
			}

			dbResult.close();
			dbStatement.close();
			DatabaseTodoConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void loadPersonalizedData(int personnelId)
	{
		addResponsibleItems(personnelId);
		addInvolvedItems(personnelId);
		addAttendeeItems(personnelId);
		addReminderItems();
		addRemainingItems();
	}

	public static int getPersonnelIdForUsername(String queryUsername)
	{
		int personnelId = 0;

		try
		{
			Connection dbConnection = DatabaseEmployeeConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT Personalnummer FROM Stammdaten WHERE logonUsername = '" + queryUsername + "'");

			if(dbResult.next())
			{
				 personnelId = dbResult.getInt("Personalnummer");
			}

			dbResult.close();
			dbStatement.close();
			DatabaseEmployeeConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}

		return personnelId;
	}

	@Override
	public int getRowCount()
	{
		return personalizedItemList.size();
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
			case -1:
				return personalizedItemList.get(rowIndex).itemId;
			case 0:
				return personalizedItemList.get(rowIndex).itemType == Todo.class ? "Protokollelement" : "Sitzung";
			case 1:
				return personalizedItemList.get(rowIndex).itemSummary;
			default:
				return "INTERNAL ERROR";
		}
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}
}
