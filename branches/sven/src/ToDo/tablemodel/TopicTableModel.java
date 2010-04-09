/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.tablemodel;

import todo.core.Topic;
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
public class TopicTableModel extends AbstractTableModel
{

	/* Themenobjekte welche zeilenweise angezeigt werden sollen */
	protected ArrayList<Topic> topicObjects = new ArrayList<Topic>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;

	/** Creates a new instance of AreaTableModel */
	public TopicTableModel()
	{
		setColumnNames();
		this.loadData();
	}

	public Object getValueAt(final int zeile, final int spalte)
	{
		switch (spalte)
		{
			case -1:
				return this.topicObjects.get(zeile).getTopicID();
			case 0:
				return this.topicObjects.get(zeile).getName();
			case 1:
				return this.topicObjects.get(zeile).getDescription();
			default:
				return null;
		}
	}


	/*
	 * return Anzahl der Bereiche
	 */
	public int getRowCount()
	{
		return this.topicObjects.size();
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
			String sql = "SELECT * FROM Thema";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				int topicID = rst.getInt("ThemaID");
				String name = rst.getString("Name");
				String description = rst.getString("Beschreibung");
				topicObjects.add(new Topic(topicID, name, description));
			}
			rst.close();
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(TopicTableModel.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Bereich");
		columnNames.add("Beschreibung");
	}
}
