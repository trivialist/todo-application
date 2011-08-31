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
import todo.db.DatabaseTodoConnect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import todo.util.DataReloadable;

public class TodoTableModel extends AbstractTableModel implements DataReloadable
{

	/* Todo-Objekte welche zeilenweise agezeigt werden sollen */
	protected ArrayList<Todo> todoObjects = new ArrayList<Todo>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;
	private int meetingID;

	/** Creates a new instance of TodoTableModel */
	public TodoTableModel(int meetingID)
	{
		this.meetingID = meetingID;
		setColumnNames();
		loadData();
	}
	
	public void reloadData()
	{
		todoObjects.clear();
		loadData();
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case -1:
				return this.todoObjects.get(zeile).getTodoID();
			case 0:
				return this.todoObjects.get(zeile).getCategory();
			case 1:
				return this.todoObjects.get(zeile).getArea();
			case 2:
				return this.todoObjects.get(zeile).getTopic();
			case 3:
				return new DateFormater(this.todoObjects.get(zeile).getReDate(), this.todoObjects.get(zeile).getReMeetingEnabled());
			/*if (reDate == null)
			{
			return "kein";
			}
			else
			{
			return sdf.format(reDate);
			}*/
			case 4:
				return this.todoObjects.get(zeile).getHeading();
			default:
				return null;
		}
	}

	/*
	 * return Anzahl der Todo-Objekte
	 */
	public int getRowCount()
	{
		return this.todoObjects.size();
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
		Todo td = new Todo();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Protokollelement WHERE Geloescht = false AND SitzungsID=" + meetingID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				td.clear();
				int tbz_id = -1;
				td.setCategoryID(rst.getInt("KategorieID"));
				td.setCategory(getCategoryByID(td.getCategoryID()));
				tbz_id = rst.getInt("TBZuordnung_ID");
				td.setTBZ_ID(tbz_id);
				td.setArea(getAreaNameByTBZ_ID(tbz_id));
				td.setTopic(getTopicNameByTBZ_ID(tbz_id));
				td.setReDate(rst.getDate("Wiedervorlagedatum"));
				td.setTodoID(rst.getInt("ToDoID"));
				td.setHeading(rst.getString("Überschrift"));
				td.setContent(rst.getString("Inhalt"));
				td.setReMeetingEnabled(rst.getBoolean("WiedervorlageGesetzt"));
				todoObjects.add(new Todo(td.getCategoryID(), td.getCategory(),
						td.getTBZ_ID(), td.getArea(),
						td.getTopic(), td.getReDate(),
						td.getTodoID(), td.getHeading(),
						td.getContent(), td.getReMeetingEnabled()));
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(TodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DatabaseTodoConnect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Kategorie");
		columnNames.add("Bereich");
		columnNames.add("Thema");
		columnNames.add("Wiedervorlage");
		columnNames.add("Überschrift");
	}

	public String getCategoryByID(int catID)
	{
		String cat = "";
		con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Kategorie WHERE KategorieID = " + catID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				cat = rst.getString("Name");
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(TodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DatabaseTodoConnect.closeDB(con);
		return cat;
	}

	public String getTopicNameByTBZ_ID(int tbz_id)
	{
		String name = "";
		con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM TBZ INNER JOIN Thema ON TBZ.ThemaID=Thema.ThemaID  " +
					"WHERE TBZ_ID=" + tbz_id;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				name = rst.getString("Name");
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(TodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DatabaseTodoConnect.closeDB(con);
		return name;
	}

	public String getAreaNameByTBZ_ID(int tbz_id)
	{
		String name = "";
		con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM TBZ INNER JOIN Bereich ON TBZ.BereichID=Bereich.BereichID " +
					"WHERE TBZ_ID=" + tbz_id;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				name = rst.getString("Name");
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(TodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DatabaseTodoConnect.closeDB(con);
		return name;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 3)
		{
			return DateFormater.class;
		}
		return super.getColumnClass(columnIndex);
	}
}
