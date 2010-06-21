/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.tablemodel;

import todo.gui.GlobalError;
import todo.core.Todo;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Hertel
 */
public class WvTodoTableModel extends AbstractTableModel
{
	protected ArrayList<Todo> ptdObjects = new ArrayList<Todo>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;

	public WvTodoTableModel(Date wvDate, int statusId)
	{
		setColumnNames();
		loadData(wvDate, statusId);
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
				return this.ptdObjects.get(zeile).getStatus();
			case 4:
				return this.ptdObjects.get(zeile).getHeading();
			default:
				return null;
		}
	}

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

	private void loadData(Date wvDate, int statusId)
	{
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql;

			if(statusId == -1)
			{
				sql = "SELECT Protokollelement.Überschrift, Status.Name as Status, Kategorie.Name as Kategorie, Thema.Name as Thema, Protokollelement.Wiedervorlagedatum, Protokollelement.Inhalt, Protokollelement.ToDoID, Protokollelement.WiedervorlageGesetzt FROM Kategorie INNER JOIN (Status INNER JOIN (Thema INNER JOIN (TBZ INNER JOIN Protokollelement ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON Thema.ThemaID = TBZ.ThemaID) ON Status.StatusID = Protokollelement.StatusID) ON Kategorie.KategorieID = Protokollelement.KategorieID WHERE Protokollelement.Wiedervorlagedatum <= #" + new SimpleDateFormat("dd/MM/yyyy").format(wvDate) + "# AND Protokollelement.Wiedervorlagedatum >= #" + new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) + "#";
			}
			else
			{
				sql = "SELECT Protokollelement.Überschrift, Status.Name as Status, Kategorie.Name as Kategorie, Thema.Name as Thema, Protokollelement.Wiedervorlagedatum, Protokollelement.Inhalt, Protokollelement.ToDoID, Protokollelement.WiedervorlageGesetzt FROM Kategorie INNER JOIN (Status INNER JOIN (Thema INNER JOIN (TBZ INNER JOIN Protokollelement ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON Thema.ThemaID = TBZ.ThemaID) ON Status.StatusID = Protokollelement.StatusID) ON Kategorie.KategorieID = Protokollelement.KategorieID WHERE Protokollelement.Wiedervorlagedatum <= #" + new SimpleDateFormat("dd/MM/yyyy").format(wvDate) + "# AND Protokollelement.Wiedervorlagedatum >= #" + new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) + "# AND Protokollelement.StatusID = " + statusId;
			}

			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				Todo td = new Todo();
				td.setTodoID(rst.getInt("ToDoID"));
				td.setTopic(rst.getString("Thema"));
				td.setCategory(rst.getString("Kategorie"));
				java.util.Date rd = rst.getDate("WiedervorlageDatum");
				td.setReMeetingEnabled(rst.getBoolean("WiedervorlageGesetzt"));
				if (rd != null)
				{
					td.setReDate(rd);
				}
				td.setContent(rst.getString("Inhalt"));
				td.setStatus(rst.getString("Status"));
				td.setHeading(rst.getString("Überschrift"));
				ptdObjects.add(td);
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(WvTodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Thema");
		columnNames.add("Kategorie");
		columnNames.add("Wiedervorlage");
		columnNames.add("Status");
		columnNames.add("Überschrift");
	}
}
