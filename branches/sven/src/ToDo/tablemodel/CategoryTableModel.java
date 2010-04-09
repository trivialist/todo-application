/*
 * CategoryTableModel.java
 *
 * Created on 30. Dezember 2006, 20:47
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package todo.tablemodel;

import todo.core.Category;
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
public class CategoryTableModel extends AbstractTableModel
{

	/* Kategorieobjekte welche zeilenweise angezeigt werden sollen */
	protected ArrayList<Category> catObjects = new ArrayList<Category>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;

	/** Creates a new instance of CategoryTableModel */
	public CategoryTableModel()
	{
		setColumnNames();
		this.loadData();
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case 0:
				return this.catObjects.get(zeile).getCatName();
			case 1:
				return this.catObjects.get(zeile).getCatDescription();
			case -1:
				return this.catObjects.get(zeile).getCatID();
			default:
				return null;
		}
	}

	/*
	 * return Anzahl der Kategorien
	 */
	public int getRowCount()
	{
		return this.catObjects.size();
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
		DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Kategorie";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				int catID = rst.getInt("KategorieID");
				String catName = rst.getString("Name");
				String catDescription = rst.getString("Beschreibung");
				catObjects.add(new Category(catID, catName, catDescription));
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(CategoryTableModel.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Kategorie");
		columnNames.add("Beschreibung");
	}
}
