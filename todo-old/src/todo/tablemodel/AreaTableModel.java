/*
 * AreaTableModel.java
 *
 * Created on 23. Juli 2007, 14:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package todo.tablemodel;

import todo.gui.GlobalError;
import todo.core.Area;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Hertel
 */
public class AreaTableModel extends AbstractTableModel
{

	/* Kategorieobjekte welche zeilenweise angezeigt werden sollen */
	protected ArrayList<Area> areaObjects = new ArrayList<Area>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;

	/** Creates a new instance of AreaTableModel */
	public AreaTableModel()
	{
		setColumnNames();
		this.loadData();
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case 0:
				return this.areaObjects.get(zeile).getAreaName();
			case 1:
				return this.areaObjects.get(zeile).getAreaDescription();
			case -1:
				return this.areaObjects.get(zeile).getAreaID();
			default:
				return null;
		}
	}

	/*
	 * return Anzahl der Bereiche
	 */
	public int getRowCount()
	{
		return this.areaObjects.size();
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
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Bereich";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				int areaID = rst.getInt("BereichID");
				String areaName = rst.getString("Name");
				String areaDescription = rst.getString("Beschreibung");
				areaObjects.add(new Area(areaID, areaName, areaDescription));
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(AreaTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Bereich");
		columnNames.add("Beschreibung");
	}
}