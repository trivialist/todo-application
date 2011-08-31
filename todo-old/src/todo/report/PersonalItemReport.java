/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */
package todo.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import todo.db.DatabaseEmployeeConnect;
import todo.db.DatabaseTodoConnect;
import todo.entity.Meeting;
import todo.gui.MainGui;
import todo.util.GlobalError;

public class PersonalItemReport
{
	private static String[] workdayNames = {"Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
	
	public void createReport(String employeeName, String itemStatus)
	{
		int empID = 0;

		if (employeeName.equals("Alle Mitarbeiter"))
		{
			empID = -1;
		}

		if (employeeName.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen des Reports. Sie haben keinen Mitarbeiter ausgewählt", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			if (itemStatus.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Fehler beim Erstellen des Reports. Sie haben keinen Status ausgewählt", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				ArrayList<HashMap> ptd;
				String reportSource = MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "PersönlicheTodos.jrxml";
				Calendar cal = Calendar.getInstance();

				HashMap<String, Object> params = new HashMap<String, Object>();
				String actDate = workdayNames[cal.get(Calendar.DAY_OF_WEEK)] + ", " + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
				params.put("Datum", actDate);
				params.put("Mitarbeiter", employeeName);
				params.put("IMAGE", MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "img\\logo_konzepte.gif");
				params.put("IMAGE2", MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "img\\wichtig.jpg");
				params.put("IMAGE3", MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "img\\info.jpg");

				if (empID == -1)
				{
					ptd = loadCompleteOpListData(itemStatus);
				}
				else
				{
					ptd = loadPersonalTodoData(itemStatus, employeeName);
				}

				if(!ptd.isEmpty())
				{
					JRMapCollectionDataSource dataSet = new JRMapCollectionDataSource(ptd);

					try
					{
						JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSet);

						JasperViewer.viewReport(jasperPrint, false);
					}
					catch (JRException ex)
					{
						Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Es wurden keine Elemente gefunden!");
				}
			}
		}
	}
	
	private ArrayList loadCompleteOpListData(String itemStatus)
	{
		ArrayList<HashMap> opData = new ArrayList<HashMap>();
		int tbz_id = -1;
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			PreparedStatement pStmt = con.prepareStatement("SELECT * FROM Protokollelement "
														   + "INNER JOIN TBZ ON Protokollelement.TBZuordnung_ID=TBZ.TBZ_ID "
														   + "WHERE Protokollelement.StatusID = ? AND Geloescht = false "
														   + "ORDER BY Wiedervorlagedatum DESC");
			pStmt.setInt(1, getFinStatusIDByName(itemStatus));
			ResultSet rst = pStmt.executeQuery();

			while (rst.next())
			{
				HashMap<String, String> fields = new HashMap<String, String>();
				tbz_id = rst.getInt("TBZuordnung_ID");
				fields.put("Kategorie", getCatByID(rst.getInt("KategorieID")));
				fields.put("Bereich", getAreaByID(getAreaIDByTBZ_ID(tbz_id)));
				fields.put("Institution", getInstByID(rst.getInt("InstitutionsID")));
				fields.put("Status", getStatByID(rst.getInt("StatusID")));
				fields.put("Thema", getTopicByID(getTopicIDByTBZ_ID(tbz_id)));
				fields.put("Inhalt", rst.getString("Inhalt"));
				fields.put("Typ", "");
				java.util.Date rd = rst.getDate("Wiedervorlagedatum");
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				if (rd != null)
				{
					fields.put("Wiedervorlagedatum", sdf.format(rd));
				}
				else
				{
					fields.put("Wiedervorlagedatum", "kein");
				}

				int todoId = rst.getInt("ToDoID");
				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", todoId)));
				fields.put("Beteiligte", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_involved_personnel", "todoID", todoId)));

				opData.add(fields);
			}

			rst.close();
			pStmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return opData;
	}
	
	private ArrayList<Integer> getPersonnelIdsFromItemId(String dbTable, String dbColumn, int searchId)
	{
		ArrayList<Integer> extractedIds = new ArrayList<Integer>();

		try
		{
			Connection con = DatabaseTodoConnect.openDB();

			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT personnelID FROM " + dbTable + " WHERE " + dbColumn + " = " + searchId);

			while (resultSet.next())
			{
				extractedIds.add(resultSet.getInt("personnelID"));
			}

			DatabaseTodoConnect.closeDB(con);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		return extractedIds;
	}
	
	private String getCatByID(int catID)
	{
		String name = "";
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Kategorie WHERE KategorieID=" + catID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				name = rst.getString("Name");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
	}
	
	private int getAreaIDByTBZ_ID(int tbzID)
	{
		if (tbzID != -1)
		{
			int id = 0;
			Connection con = DatabaseTodoConnect.openDB();

			try
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT * FROM TBZ WHERE TBZ_ID=" + tbzID;
				ResultSet rst = stmt.executeQuery(sql);

				while (rst.next())
				{
					id = rst.getInt("BereichID");
				}

				rst.close();
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}

			DatabaseTodoConnect.closeDB(con);
			return id;
		}
		else
		{
			return -1;
		}

	}
	
	private String getAreaByID(int areaID)
	{
		if (areaID != -1)
		{
			String name = "";
			Connection con = DatabaseTodoConnect.openDB();

			try
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT * FROM Bereich WHERE BereichID=" + areaID;
				ResultSet rst = stmt.executeQuery(sql);

				while (rst.next())
				{
					name = rst.getString("Name");
				}

				rst.close();
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}

			DatabaseTodoConnect.closeDB(con);
			return name;
		}
		else
		{
			return "kein";
		}

	}
	
	private String getInstByID(int instID)
	{
		String name = "";
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Institution WHERE InstitutionID=" + instID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				name = rst.getString("Name");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
	}

	private String getStatByID(int statID)
	{
		String name = "";
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Status WHERE StatusID=" + statID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				name = rst.getString("Name");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
	}
	
	private int getTopicIDByTBZ_ID(int tbzID)
	{
		if (tbzID != -1)
		{
			int id = 0;
			Connection con = DatabaseTodoConnect.openDB();

			try
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT * FROM TBZ WHERE TBZ_ID=" + tbzID;
				ResultSet rst = stmt.executeQuery(sql);

				while (rst.next())
				{
					id = rst.getInt("ThemaID");
				}

				rst.close();
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}

			DatabaseTodoConnect.closeDB(con);
			return id;
		}
		else
		{
			return -1;
		}

	}
	
	private String getTopicByID(int topicID)
	{
		if (topicID != -1)
		{
			String name = "";
			Connection con = DatabaseTodoConnect.openDB();

			try
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT * FROM Thema WHERE ThemaID=" + topicID;
				ResultSet rst = stmt.executeQuery(sql);

				while (rst.next())
				{
					name = rst.getString("Name");
				}

				rst.close();
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}

			DatabaseTodoConnect.closeDB(con);
			return name;
		}
		else
		{
			return "kein";
		}

	}
	
	private int getFinStatusIDByName(String statusName)
	{
		int statID = 0;
		Connection con = DatabaseTodoConnect.openDB();

		if (statusName.equals("Alle"))
		{
			return -1;
		}

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Status WHERE Name='" + statusName + "'";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				statID = rst.getInt("StatusID");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return statID;
	}
	
	private String getNameAndLastNameByID(ArrayList<Integer> ids)
	{
		StringBuilder participantsBuffer = new StringBuilder();
		Connection con = DatabaseEmployeeConnect.openDB();

		for (int id : ids)
		{
			try
			{
				Statement stmt = con.createStatement();
				String sql = "SELECT Nachname, Vorname FROM Stammdaten WHERE Personalnummer = " + id;
				ResultSet rst = stmt.executeQuery(sql);

				while (rst.next())
				{
					participantsBuffer.append(rst.getString("Vorname")).append(" ").append(rst.getString("Nachname")).append(", ");
				}

				rst.close();
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}

		}
		
		DatabaseTodoConnect.closeDB(con);
		String participantsString = participantsBuffer.length() > 0 ? participantsBuffer.substring(0, participantsBuffer.length() - 2).toString() : "";
		
		return participantsString;
	}
	
	private ArrayList loadPersonalTodoData(String status, String employee)
	{
		ArrayList<HashMap> personalTodoData = new ArrayList<HashMap>();
		int tbz_id = -1;
		int empID = getEmployeeIDByName(employee);
		String sql = "";
		String sql2 = "";

		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			if (status.equals("Alle"))
			{
				sql = "SELECT Protokollelement.* FROM Protokollelement INNER JOIN todo_responsible_personnel ON "
					  + "Protokollelement.ToDoID = todo_responsible_personnel.todoID "
					  + "WHERE todo_responsible_personnel.personnelID = " + empID + " AND Protokollelement.Geloescht = false";
			}
			else
			{
				sql = "SELECT Protokollelement.* FROM Protokollelement INNER JOIN todo_responsible_personnel ON "
					  + "Protokollelement.ToDoID = todo_responsible_personnel.todoID "
					  + "WHERE todo_responsible_personnel.personnelID = " + empID + " AND Protokollelement.Geloescht = false "
					  + "AND Protokollelement.StatusID = " + getFinStatusIDByName(status);
			}

			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				HashMap<String, String> fields = new HashMap<String, String>();
				int meetingID = rst.getInt("SitzungsID");
				Meeting m = getMeetingDataByID(meetingID);
				tbz_id = rst.getInt("TBZuordnung_ID");
				fields.put("Typ", "Verantwortlich");
				fields.put("Kategorie", getCatByID(rst.getInt("KategorieID")));
				fields.put("Bereich", getAreaByID(getAreaIDByTBZ_ID(tbz_id)));
				fields.put("Institution", getInstByID(rst.getInt("InstitutionsID")));
				fields.put("Status", getStatByID(rst.getInt("StatusID")));
				fields.put("Thema", getTopicByID(getTopicIDByTBZ_ID(tbz_id)));
				fields.put("Inhalt", rst.getString("Inhalt"));
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				if (rst.getBoolean("WiedervorlageGesetzt"))
				{
					fields.put("Wiedervorlagedatum", sdf.format(rst.getDate("Wiedervorlagedatum")));
				}
				else
				{
					fields.put("Wiedervorlagedatum", "kein");
				}

				int todoId = rst.getInt("ToDoID");
				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", todoId)));
				fields.put("Beteiligte", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_involved_personnel", "todoID", todoId)));
				fields.put("SitzOrt", m.getPlace());
				fields.put("SitzDatum", sdf.format(m.getDate()));
				fields.put("SitzName", m.getMeetingType());
				personalTodoData.add(fields);
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		try
		{
			Statement stmt2 = con.createStatement();
			if (status.equals("Alle"))
			{
				sql2 = "SELECT Protokollelement.* FROM Protokollelement INNER JOIN todo_involved_personnel ON "
					   + "Protokollelement.ToDoID = todo_involved_personnel.todoID "
					   + "WHERE todo_involved_personnel.personnelID = " + empID + " AND Protokollelement.Geloescht = false";
			}
			else
			{
				sql2 = "SELECT Protokollelement.* FROM Protokollelement INNER JOIN todo_involved_personnel ON "
					   + "Protokollelement.ToDoID = todo_involved_personnel.todoID "
					   + "WHERE todo_involved_personnel.personnelID = " + empID + " AND Protokollelement.Geloescht = false "
					   + "AND StatusID = " + getFinStatusIDByName(status);
			}

			ResultSet rst2 = stmt2.executeQuery(sql2);

			while (rst2.next())
			{
				int meetingID = rst2.getInt("SitzungsID");
				Meeting m = getMeetingDataByID(meetingID);
				HashMap<String, String> fields = new HashMap<String, String>();
				fields.put("Typ", "Beteiligt");
				fields.put("Kategorie", getCatByID(rst2.getInt("KategorieID")));
				fields.put("Bereich", getAreaByID(rst2.getInt("BereichID")));
				fields.put("Institution", getInstByID(rst2.getInt("InstitutionsID")));
				fields.put("Status", getStatByID(rst2.getInt("StatusID")));
				fields.put("Thema", rst2.getString("Thema"));
				fields.put("Inhalt", rst2.getString("Inhalt"));
				fields.put("Wiedervorlagedatum", rst2.getString("Wiedervorlagedatum"));
				int todoId = rst2.getInt("ToDoID");
				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", todoId)));
				fields.put("Beteiligte", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_involved_personnel", "todoID", todoId)));
				fields.put("SitzOrt", m.getPlace());
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				fields.put("SitzDatum", sdf.format(m.getDate()));
				fields.put("SitzName", m.getMeetingType());
				personalTodoData.add(fields);
			}

			rst2.close();
			stmt2.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return personalTodoData;
	}
	
	private Meeting getMeetingDataByID(int meetingID)
	{
		Meeting m = new Meeting();
		m.setMeetingID(meetingID);
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsdaten WHERE SitzungsdatenID = " + m.getMeetingID();
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				int tmp = rst.getInt("SitzungsartID");
				m.setMeetingType(getMeetingTypeByID(tmp));
				m.setPlace(rst.getString("Ort"));
				m.setDate(rst.getDate("Datum"));
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return m;
	}
	
	private String getMeetingTypeByID(int id)
	{
		String mType = "";
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsart WHERE SitzungsartID=" + id;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				mType = rst.getString("Name");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return mType;
	}
	
	private int getEmployeeIDByName(String name)
	{
		int empID = -1;
		String parts[] = name.split(", ");
		Connection con = DatabaseEmployeeConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT Nachname, Vorname, Personalnummer FROM Stammdaten "
						 + "WHERE Nachname LIKE '" + parts[0] + "' AND Vorname LIKE '" + parts[1] + "'";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				empID = rst.getInt("Personalnummer");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(PersonalItemReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return empID;
	}
}
