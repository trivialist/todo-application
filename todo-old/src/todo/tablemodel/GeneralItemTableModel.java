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

import todo.util.DateFormater;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import todo.entity.Meeting;
import todo.db.DatabaseTodoConnect;
import todo.util.DataReloadable;

public class GeneralItemTableModel extends AbstractTableModel implements DataReloadable
{
	private static final String columnNames[] = {"Datum", "Ort", "Sitzungsart"};
	private ArrayList<Meeting> generalItemList = new ArrayList<Meeting>();

	public GeneralItemTableModel()
	{
		loadGeneralData();
	}

	private void loadGeneralData()
	{
		try
		{
			Connection dbConnection = DatabaseTodoConnect.openDB();
			Statement dbStatement = dbConnection.createStatement();
			ResultSet dbResult = dbStatement.executeQuery("SELECT * FROM Sitzungsdaten INNER JOIN Sitzungsart ON Sitzungsdaten.SitzungsartID = Sitzungsart.SitzungsartID WHERE Geloescht = false ORDER BY Datum DESC");

			while(dbResult.next())
			{
				Meeting newItem = new Meeting();

				newItem.setMeetingID(dbResult.getInt("SitzungsdatenID"));
				newItem.setDate(dbResult.getDate("Datum"));
				newItem.setPlace(dbResult.getString("Ort"));
				newItem.setMeetingType(dbResult.getString("Name"));

				generalItemList.add(newItem);
			}

			dbResult.close();
			dbStatement.close();
			DatabaseTodoConnect.closeDB(dbConnection);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(GeneralItemTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void reloadData()
	{
		generalItemList.clear();
		loadGeneralData();
	}

	@Override
	public int getRowCount()
	{
		return generalItemList.size();
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
				return generalItemList.get(rowIndex).getMeetingID();
			case 0:
				return new DateFormater(generalItemList.get(rowIndex).getDate());
			case 1:
				return generalItemList.get(rowIndex).getPlace();
			case 2:
				return generalItemList.get(rowIndex).getMeetingType();
			default:
				return "INTERNAL ERROR";
		}
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 0)
		{
			return DateFormater.class;
		}

		return super.getColumnClass(columnIndex);
	}
}
