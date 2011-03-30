/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.tablemodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import todo.gui.GlobalError;
import todo.core.Todo;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sven Skrabal
 */
public class OpListTableModel extends AbstractTableModel
{
	protected ArrayList<Todo> ptdObjects = new ArrayList<Todo>();
	private Vector<String> columnNames = new Vector<String>();
	private static Connection con;

	public OpListTableModel(int meetingTypeId, Date wvDate)
	{
		setColumnNames();
		loadData(meetingTypeId, wvDate);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 1)
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
				return this.ptdObjects.get(zeile).getCategory();
			case 1:
				return new DateFormater(this.ptdObjects.get(zeile).getReDate(), this.ptdObjects.get(zeile).getReMeetingEnabled());
			case 2:
				return this.ptdObjects.get(zeile).getHeading();
			case 3:
				return getTextByNumberOfCharacters(this.ptdObjects.get(zeile).getContent(), 100);
			default:
				return null;
		}
	}

	private String getTextByNumberOfCharacters(String text, int numberOfCharacters)
	{
		Pattern p = Pattern.compile("([^ ]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		String returnText = "";

		int characterCount = 0;
		while(m.find())
		{
			String newWord = m.group(1);
			newWord = newWord.replaceAll("\n|\r", "");

			if(characterCount + newWord.length() < numberOfCharacters)
			{
				returnText += newWord + " ";
				characterCount += newWord.length() + 1;
			}
			else
			{
				break;
			}
		}

		return returnText + "...";
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

	private void loadData(int meetingTypeId, Date wvDate)
	{
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT Sitzungsart.Name as Sitzungsart, Protokollelement.WV_Sitzungsart, Protokollelement.�berschrift, "
						  + "Status.Name as Status, Kategorie.Name as Kategorie, Thema.Name as Thema, Protokollelement.Wiedervorlagedatum, "
						  + "Protokollelement.Inhalt, Protokollelement.ToDoID, Protokollelement.WiedervorlageGesetzt "
						  + "FROM Thema "
						  + "INNER JOIN (TBZ "
						  + "INNER JOIN (Sitzungsart "
						  + "INNER JOIN (Sitzungsdaten "
						  + "INNER JOIN (Status "
						  + "INNER JOIN (Kategorie "
						  + "INNER JOIN Protokollelement "
						  + "ON Kategorie.KategorieID = Protokollelement.KategorieID) "
						  + "ON Status.StatusID = Protokollelement.StatusID) "
						  + "ON Sitzungsdaten.SitzungsdatenID = Protokollelement.SitzungsID) "
						  + "ON Sitzungsart.SitzungsartID = Sitzungsdaten.SitzungsartID) "
						  + "ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) "
						  + "ON Thema.ThemaID = TBZ.ThemaID "
						  + "WHERE Protokollelement.WV_Sitzungsart = " + meetingTypeId + " AND Status.Name = 'Neu' AND WiedervorlageGesetzt = true "
						  + "AND WiedervorlageDatum <= #" + new SimpleDateFormat("dd/MM/yyyy").format(wvDate) + "#";
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
				td.setHeading(rst.getString("�berschrift"));
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
			Logger.getLogger(TopicTodoTableModel.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public void setColumnNames()
	{
		columnNames.add("Kategorie");
		columnNames.add("Wiedervorlage");
		columnNames.add("�berschrift");
		columnNames.add("Inhalt");
	}
}
