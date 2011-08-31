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

public class ShortProtocolReport
{
	public void createReport(int meetingId)
	{
		String reportSource = MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "ProtokollKurz.jrxml";

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
			Logger.getLogger(ShortProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		params.put("SitzDatum", sdf.format(actMeeting.getDate()));
		params.put("SitzName", actMeeting.getMeetingType());
		params.put("SitzOrt", actMeeting.getPlace());
		params.put("SitzParticipiants", getParticipiants(actMeeting.getMeetingID()));
		ArrayList<HashMap> std = loadShortTodoData(actMeeting.getMeetingID());

		JRMapCollectionDataSource dataSet = new JRMapCollectionDataSource(std);

		try
		{
			JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSet);

			JasperViewer.viewReport(jasperPrint, false);
		}
		catch (JRException ex)
		{
			Logger.getLogger(ShortProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private ArrayList loadShortTodoData(int meetingId)
	{
		ArrayList<HashMap> shortTodoData = new ArrayList<HashMap>();
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Protokollelement WHERE Geloescht = false AND SitzungsID = " + meetingId;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				HashMap<String, String> fields = new HashMap<String, String>();
				fields.put("TOP", rst.getString("Überschrift"));
				fields.put("Inhalt", rst.getString("Inhalt"));
				if (rst.getBoolean("WiedervorlageGesetzt"))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					fields.put("Wiedervorlagedatum", sdf.format(rst.getDate("Wiedervorlagedatum")));
				}
				else
				{
					fields.put("Wiedervorlagedatum", "kein");
				}

				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", rst.getInt("ToDoID"))));
				shortTodoData.add(fields);
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(ShortProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return shortTodoData;
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
				Logger.getLogger(ShortProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		DatabaseTodoConnect.closeDB(con);
		String participantsString = participantsBuffer.length() > 0 ? participantsBuffer.substring(0, participantsBuffer.length() - 2).toString() : "";

		return participantsString;
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
			Logger.getLogger(ShortProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
		}

		return extractedIds;
	}

	private String getParticipiants(int meetingId)
	{
		Connection con = DatabaseTodoConnect.openDB();
		Connection con2 = DatabaseEmployeeConnect.openDB();
		String returnString = "";

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT meeting_attendee_personnel.personnelID FROM meeting_attendee_personnel "
						 + "INNER JOIN Sitzungsdaten ON meeting_attendee_personnel.meetingID = Sitzungsdaten.SitzungsdatenID "
						 + "WHERE Sitzungsdaten.SitzungsdatenID = " + meetingId;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				Statement stmt2 = con2.createStatement();
				String sql2 = "SELECT * FROM Stammdaten WHERE Personalnummer = " + rst.getInt("personnelID");
				ResultSet rst2 = stmt2.executeQuery(sql2);
				rst2.next();

				returnString += rst2.getString("Vorname") + " " + rst2.getString("Nachname") + ", ";

				rst2.close();
				stmt2.close();
			}

			rst.close();
			stmt.close();

		}
		catch (Exception ex)
		{
			Logger.getLogger(ShortProtocolReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		DatabaseEmployeeConnect.closeDB(con2);

		return (returnString.length() > 2) ? returnString.substring(0, returnString.length() - 2) : "";
	}
}
