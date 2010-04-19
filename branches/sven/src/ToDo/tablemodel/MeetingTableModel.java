/*
 * MeetingTableModel.java
 *
 * Created on 9. Januar 2007, 19:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package todo.tablemodel;

import todo.gui.GlobalError;
import todo.core.Meeting;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Hertel
 */
public class MeetingTableModel extends AbstractTableModel
{

	/* Sitzungdaten-Objekte welche zeilenweise agezeigt werden sollen */
	protected ArrayList<Meeting> meetingObjects = new ArrayList<Meeting>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;

	/** Creates a new instance of MeetingTableModel */
	public MeetingTableModel()
	{
		setColumnNames();
		this.loadData("", "");
	}

	public MeetingTableModel(String keyword, String field)
	{
		setColumnNames();
		this.loadData(keyword, field);
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case 0:
				return new DateFormater(this.meetingObjects.get(zeile).getDate());
			case 1:
				return this.meetingObjects.get(zeile).getPlace();
			case 2:
				return this.meetingObjects.get(zeile).getMeetingType();
			case -1:
				return this.meetingObjects.get(zeile).getMeetingID();
			default:
				return null;
		}
	}

	/*
	 * return Anzahl der Sitzungsarten-Objekte
	 */
	public int getRowCount()
	{
		return this.meetingObjects.size();
	}

	public int getColumnCount()
	{
		return this.columnNames.size();
	}

	public String getColumnName(final int spalte)
	{
		if (spalte < this.getColumnCount())
		{
			return columnNames.elementAt(spalte);
		}
		else
		{
			return super.getColumnName(spalte);
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	protected void loadData(String keyword, String field)
	{
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		String whereCondition = "";
		if (!keyword.equals("") && !field.equals(""))
		{
			if (field.equals("Sitzungsart"))
			{
				field = "Name";
			}

			if (field.equals("Jahr"))
			{
				whereCondition = "AND Datum BETWEEN #01/01/" + keyword + "# AND #31/12/" + keyword + "#";
			}
			else
			{
				whereCondition = "AND (" + field + " LIKE '%" + keyword + "' OR " + field + " LIKE '" + keyword + "%' OR " + field + " LIKE '%" + keyword + "%')";
			}
		}

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsart INNER JOIN Sitzungsdaten ON Sitzungsart.SitzungsartID = Sitzungsdaten.SitzungsartID WHERE Geloescht = false " + whereCondition + " ORDER BY Datum DESC";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				Date date = rst.getDate("Datum");
				String place = rst.getString("Ort");
				int meetingID = rst.getInt("SitzungsdatenID");
				int meetingTypeID = rst.getInt("SitzungsartID");
				String meetingType = "";
				Statement stmt2 = con.createStatement();
				String sql2 = "SELECT * FROM Sitzungsart WHERE SitzungsartID = " +
						meetingTypeID;
				ResultSet rst2 = stmt2.executeQuery(sql2);

				while (rst2.next())
				{
					meetingType = rst2.getString("Name");
				}
				meetingObjects.add(new Meeting(date, place, meetingType, meetingID));
				rst2.close();
				stmt2.close();
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(MeetingTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Datum");
		columnNames.add("Ort");
		columnNames.add("Sitzungsart");
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
