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
import todo.util.GlobalError;
import todo.entity.Todo;
import todo.entity.Employee;
import todo.db.DatabaseTodoConnect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonalTodoTableModel extends AbstractTableModel
{
	/* Sitzungdaten-Objekte welche zeilenweise agezeigt werden sollen */
	protected ArrayList<Todo> ptdObjects = new ArrayList<Todo>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;
	private int status = -1;
	private String sStat = ""; //falls als status -1 übergeben wird, wurde Status="Alle" gewählt
	//in Sql-Abfrage sollen also alle Datensätze ermittelt werden -> kein Status in where-klausel
	private Employee emp = new Employee();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	/** Creates a new instance of MeetingTableModel */
	public PersonalTodoTableModel(int employee, int stat)
	{
		setColumnNames();
		emp.setEmployeeID(employee);
		this.status = stat;
		if (emp.getEmployeeID() == -1)
		{
			if (status != -1)
			{
				if (status != 0)
				{
					sStat = " AND Geloescht = false AND Protokollelement.StatusID = " + status;
				}
			}
			this.loadOpData();
		}
		else
		{
			if (status != -1)
			{
				if (status != 0)
				{
					sStat = " AND Geloescht = false AND Protokollelement.StatusID = " + status;
				}
			}
			this.loadData();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 2)
		{
			return DateFormater.class;
		}
		else
		{
			return String.class;
		}
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case -1:
				return this.ptdObjects.get(zeile).getTodoID();
			case 0:
				return this.ptdObjects.get(zeile).getTopic();
			case 1:
				return this.ptdObjects.get(zeile).getCategory();
			case 2:
				return new DateFormater(this.ptdObjects.get(zeile).getReDate(), this.ptdObjects.get(zeile).getReMeetingEnabled());
			case 3:
				return this.ptdObjects.get(zeile).getReMeetingType();
			case 4:
				return this.ptdObjects.get(zeile).getStatus();
			case 5:
				return this.ptdObjects.get(zeile).getHeading();
			default:
				return null;
		}
	}

	/*
	 * return Anzahl der Sitzungsarten-Objekte
	 */
	public int getRowCount()
	{
		return this.ptdObjects.size();
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
		con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT Protokollelement.WV_Sitzungsart, Protokollelement.Überschrift, Protokollelement.ToDoID AS ToDoID, "
						 + "Thema.Name AS Thema, Kategorie.Name AS Kategorie, Protokollelement.Wiedervorlagedatum AS WV, "
						 + "Protokollelement.WiedervorlageGesetzt AS WiedervorlageGesetzt, Protokollelement.Inhalt AS Inhalt, "
						 + "Status.Name AS Status, todo_responsible_personnel.personnelID FROM (Kategorie INNER JOIN "
						 + "((Thema INNER JOIN TBZ ON Thema.ThemaID = TBZ.ThemaID) INNER JOIN (Status INNER JOIN Protokollelement "
						 + "ON Status.StatusID = Protokollelement.StatusID) ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON "
						 + "Kategorie.KategorieID = Protokollelement.KategorieID) INNER JOIN todo_responsible_personnel ON "
						 + "Protokollelement.ToDoID = todo_responsible_personnel.todoID "
						 + "WHERE (todo_responsible_personnel.personnelID = " + emp.getEmployeeID() + ")" + sStat;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				Todo td = new Todo();
				td.setTodoID(rst.getInt("ToDoID"));
				td.setTopic(rst.getString("Thema"));
				td.setCategory(rst.getString("Kategorie"));
				java.util.Date rd = rst.getDate("WV");
				td.setReMeetingEnabled(rst.getBoolean("WiedervorlageGesetzt"));
				if (rd != null)
				{
					td.setReDate(rd);
				}
				td.setContent(rst.getString("Inhalt"));
				td.setStatus(rst.getString("Status"));
				td.setHeading(rst.getString("Überschrift"));
				int id = rst.getInt("WV_Sitzungsart");
				if (td.getReMeetingEnabled() && id != -1)
				{
					Statement stmt2 = con.createStatement();
					sql = "SELECT Name FROM Sitzungsart WHERE SitzungsartID = " + id;
					ResultSet rst2 = stmt2.executeQuery(sql);
					rst2.next();
					td.setReMeetingType(rst2.getString("Name"));
				}
				else
				{
					td.setReMeetingType("");
				}
				ptdObjects.add(td);
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalTodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DatabaseTodoConnect.closeDB(con);
	}

	protected void loadOpData()
	{
		con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT Protokollelement.WV_Sitzungsart, Protokollelement.Überschrift, Protokollelement.ToDoID as ToDoID, Thema.Name as Thema, Kategorie.Name as Kategorie, "
						 + "Protokollelement.Wiedervorlagedatum as WV, Protokollelement.WiedervorlageGesetzt as WiedervorlageGesetzt, Protokollelement.Inhalt as Inhalt, Status.Name as Status "
						 + "FROM Kategorie INNER JOIN "
						 + "((Thema INNER JOIN TBZ ON Thema.ThemaID = TBZ.ThemaID) "
						 + "INNER JOIN (Status INNER JOIN Protokollelement ON Status.StatusID = Protokollelement.StatusID) "
						 + "ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON Kategorie.KategorieID = Protokollelement.KategorieID "
						 + "WHERE (Kategorie.Name <> 'Information' OR (Kategorie.Name = 'Information' AND WiedervorlageGesetzt = true)) " + sStat;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				Todo td = new Todo();
				td.setTodoID(rst.getInt("ToDoID"));
				td.setTopic(rst.getString("Thema"));
				td.setCategory(rst.getString("Kategorie"));
				java.util.Date rd = rst.getDate("WV");
				td.setReMeetingEnabled(rst.getBoolean("WiedervorlageGesetzt"));
				if (rd != null)
				{
					td.setReDate(rd);
				}
				td.setContent(rst.getString("Inhalt"));
				td.setStatus(rst.getString("Status"));
				td.setHeading(rst.getString("Überschrift"));
				int id = rst.getInt("WV_Sitzungsart");
				if (td.getReMeetingEnabled() && id != -1)
				{
					Statement stmt2 = con.createStatement();
					sql = "SELECT Name FROM Sitzungsart WHERE SitzungsartID = " + id;
					ResultSet rst2 = stmt2.executeQuery(sql);
					rst2.next();
					td.setReMeetingType(rst2.getString("Name"));
				}
				else
				{
					td.setReMeetingType("");
				}
				ptdObjects.add(td);
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalTodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DatabaseTodoConnect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Thema");
		columnNames.add("Kategorie");
		columnNames.add("Wiedervorlage");
		columnNames.add("WV-Sitzungsart");
		columnNames.add("Status");
		columnNames.add("Überschrift");
	}
}
