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

import todo.util.GlobalError;
import todo.entity.Employee;
import todo.db.DatabaseEmployeeConnect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipantsTableModel extends AbstractTableModel
{

	/* Sitzungsarten-Objekte welche zeilenweise agezeigt werden sollen */
	protected ArrayList<Employee> employeeObjects = new ArrayList<Employee>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;
	private ArrayList<Integer> participants = new ArrayList<Integer>();
	private int meetingID;

	/** Creates a new instance of ParticipantsTableModel */
	public ParticipantsTableModel(ArrayList<Integer> participants, int meetingID)
	{
		this.participants = participants;
		this.meetingID = meetingID;
		setColumnNames();
		this.loadData();
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case -1:
				return this.employeeObjects.get(zeile).getEmployeeID();
			case 0:
				return this.employeeObjects.get(zeile).getLastName();
			case 1:
				return this.employeeObjects.get(zeile).getName();
			default:
				return null;
		}
	}

	/*
	 * return Anzahl der Sitzungsarten-Objekte
	 */
	public int getRowCount()
	{
		return this.employeeObjects.size();
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

	protected void loadData()
	{
		con = DatabaseEmployeeConnect.openDB();

		for(int employeeID : participants)
		{
			try
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT * FROM Stammdaten WHERE Personalnummer = " + employeeID + " ORDER BY Nachname ASC";
				ResultSet rst = stmt.executeQuery(sql);

				while (rst.next())
				{
					String lastName = rst.getString("Nachname");
					String name = rst.getString("Vorname");
					employeeObjects.add(new Employee(employeeID, name, lastName));
				}
				rst.close();
				stmt.close();
			} catch (Exception ex)
			{
				Logger.getLogger(ParticipantsTableModel.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}
		}
		DatabaseEmployeeConnect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Nachname");
		columnNames.add("Vorname");
	}
}
