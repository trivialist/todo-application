/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedata;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author sven
 */
public class MigrateTableModel implements TableModel
{
	Vector<String> colNames = new Vector<String>();
	ArrayList<Element> data = new ArrayList<Element>();
	int meetingId;

	public class Element
	{
		public String thema;
		public String inhalt;
		public String heading;
		public int id;
	}

	public MigrateTableModel(int sitzung)
	{
		colNames.add("Thema");
		colNames.add("Inhalt");
		colNames.add("Überschrift");

		meetingId = sitzung;

		if(meetingId == -1)return;

		DB_ToDo_Connect.openDB();
		Connection con = DB_ToDo_Connect.getCon();
		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT Thema, Inhalt, Überschrift, ToDoID FROM Protokollelement WHERE updated = false AND SitzungsID = " + meetingId;
			ResultSet rst = stmt.executeQuery(sql);

			while(rst.next())
			{
				Element tmp = new Element();

				tmp.thema = rst.getString("Thema");
				tmp.inhalt = rst.getString("Inhalt");
				tmp.heading = rst.getString("Überschrift");
				tmp.id = rst.getInt("ToDoID");
				
				data.add(tmp);
			}

		} catch (SQLException ex)
		{
			Logger.getLogger(MigrateTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public int getRowCount()
	{
		return data.size();
	}

	public int getColumnCount()
	{
		return colNames.size();
	}

	public String getColumnName(int columnIndex)
	{
		return colNames.get(columnIndex);
	}

	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return (columnIndex == 2) ? true : false;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch(columnIndex)
		{
			case 0:
				return data.get(rowIndex).thema;
			case 1:
				return data.get(rowIndex).inhalt;
			case 2:
				return data.get(rowIndex).heading;
		}

		return null;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		if(columnIndex == 2)
		{
			data.get(rowIndex).heading = (String)aValue;
		}
	}

	public void addTableModelListener(TableModelListener l)
	{
	}

	public void removeTableModelListener(TableModelListener l)
	{
	}

	public void saveModifiedModel()
	{

	}
}
