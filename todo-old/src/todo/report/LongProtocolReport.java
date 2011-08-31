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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class LongProtocolReport
{
	public void createReport(int meetingId)
	{
		String reportSource = MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "Protokoll.jrxml";
		Connection con = DatabaseTodoConnect.openDB();
		Meeting actMeeting = new Meeting();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsdaten WHERE SitzungsdatenID = " + meetingId;
			ResultSet rst = stmt.executeQuery(sql);
			rst.next();

			actMeeting.setMeetingID(rst.getInt("SitzungsdatenID"));
			actMeeting.setDate(rst.getDate("Datum"));
			actMeeting.setMeetingTypeID(rst.getInt("SitzungsartID"));
			actMeeting.setPlace(rst.getString("Ort"));
			actMeeting.setProt(rst.getInt("Protokollant"));

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		ArrayList<Integer> prot = new ArrayList<Integer>();
		prot.add(actMeeting.getProt());
		String agenda = "";

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT Überschrift FROM Protokollelement WHERE Geloescht = false AND "
						 + "SitzungsID = " + actMeeting.getMeetingID() + " ORDER BY ToDoID ASC";

			ResultSet rst = stmt.executeQuery(sql);

			int counter = 1;
			while (rst.next())
			{
				agenda += "TOP " + counter + " " + rst.getString("Überschrift") + "\n";
				counter++;
			}

			rst.close();
			stmt.close();

		}
		catch (Exception ex)
		{
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		DatabaseTodoConnect.closeDB(con);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Tagesordnung", agenda.toString());
		params.put("SitzName", actMeeting.getMeetingType());
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		params.put("Datum", sdf.format(actMeeting.getDate()));
		params.put("Ort", actMeeting.getPlace());
		params.put("Protokollant", getNameAndLastNameByID(prot));
		params.put("Teilnehmer", getNameAndLastNameByID(getPersonnelIdsFromItemId("meeting_attendee_personnel", "meetingID", actMeeting.getMeetingID())));
		params.put("Sonstige", actMeeting.getOtherPaticipants());
		params.put("IMAGE", MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "img\\logo_konzepte.gif");
		params.put("Agenda", agenda);
		ArrayList<HashMap> td = loadTodoData(actMeeting.getMeetingID());

		JRMapCollectionDataSource dataSet = new JRMapCollectionDataSource(td);

		try
		{
			JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSet);
			JasperViewer.viewReport(jasperPrint, false);
		}
		catch (JRException ex)
		{
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private ArrayList loadTodoData(int meetingId)
	{
		ArrayList<HashMap> todoData = new ArrayList<HashMap>();
		int tbz_id = -1;
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Protokollelement WHERE Geloescht = false AND SitzungsID="
						 + meetingId + " ORDER BY ToDoID ASC";
			ResultSet rst = stmt.executeQuery(sql);
			int counter = 1;

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
				fields.put("Ueberschrift", "TOP " + counter + " " + rst.getString("Überschrift"));
				if (rst.getBoolean("WiedervorlageGesetzt"))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					fields.put("Wiedervorlagedatum", sdf.format(rst.getDate("Wiedervorlagedatum")));
				}
				else
				{
					fields.put("Wiedervorlagedatum", "kein");
				}

				int todoId = rst.getInt("ToDoID");
				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", todoId)));
				fields.put("Beteiligte", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_involved_personnel", "todoID", todoId)));
				todoData.add(fields);
				counter++;
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		DatabaseTodoConnect.closeDB(con);
		return todoData;
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
				Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
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
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
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
			Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		return extractedIds;
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
				Logger.getLogger(LongProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		DatabaseTodoConnect.closeDB(con);
		String participantsString = participantsBuffer.length() > 0 ? participantsBuffer.substring(0, participantsBuffer.length() - 2).toString() : "";

		return participantsString;
	}
}
