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
import java.util.Calendar;
import java.util.Date;
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
import todo.gui.MainGui;
import todo.util.GlobalError;

public class OpenTodoReport
{
	private static String[] workdayNames = {"Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
	
	public void createReport(String reportMeetingType, Date reminderReport)
	{
		if (reportMeetingType.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Das Feld für die Auswahl der Sitzungsart ist leer bzw. wurde nicht gewählt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			ArrayList<HashMap> ptd;
			String reportSource = MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "MeetingTypeTodos.jrxml";
			Calendar cal = Calendar.getInstance();

			HashMap<String, Object> params = new HashMap<String, Object>();
			String actDate = workdayNames[cal.get(Calendar.DAY_OF_WEEK)] + ", " + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
			params.put("Datum", actDate);
			params.put("Sitzungsart", reportMeetingType.toString());
			params.put("IMAGE", MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "img\\logo_konzepte.gif");

			ptd = loadMeetingTypeData(reminderReport, getMeetingTypeIDByName(reportMeetingType));

			JRMapCollectionDataSource dataSet = new JRMapCollectionDataSource(ptd);

			try
			{
				JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSet);
				JasperViewer.viewReport(jasperPrint, false);
			}
			catch (JRException ex)
			{
				Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	private ArrayList<HashMap> loadMeetingTypeData(java.util.Date wvDate, int meetingTypeId)
	{
		Connection con = DatabaseTodoConnect.openDB();
		ArrayList<HashMap> data = new ArrayList<HashMap>();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT SitzungsID, Überschrift, Protokollelement.InstitutionsID, TBZuordnung_ID, Sitzungsart.Name as Sitzungsart, Protokollelement.WV_Sitzungsart, Protokollelement.Überschrift, Status.Name as Status, Kategorie.Name as Kategorie, Thema.Name as Thema, Protokollelement.Wiedervorlagedatum, Protokollelement.Inhalt, Protokollelement.ToDoID, Protokollelement.WiedervorlageGesetzt FROM Thema INNER JOIN (TBZ INNER JOIN (Sitzungsart INNER JOIN (Sitzungsdaten INNER JOIN (Status INNER JOIN (Kategorie INNER JOIN Protokollelement ON Kategorie.KategorieID = Protokollelement.KategorieID) ON Status.StatusID = Protokollelement.StatusID) ON Sitzungsdaten.SitzungsdatenID = Protokollelement.SitzungsID) ON Sitzungsart.SitzungsartID = Sitzungsdaten.SitzungsartID) ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON Thema.ThemaID = TBZ.ThemaID WHERE Sitzungsart.SitzungsartID = " + meetingTypeId + " AND Status.Name = 'Neu' AND WiedervorlageGesetzt = true AND WiedervorlageDatum <= #" + new SimpleDateFormat("dd/MM/yyyy").format(wvDate) + "#";
			ResultSet rst = stmt.executeQuery(sql);
			Statement stmt2 = con.createStatement();

			while (rst.next())
			{
				HashMap<String, String> fields = new HashMap<String, String>();

				int tbz_id = rst.getInt("TBZuordnung_ID");
				fields.put("Kategorie", rst.getString("Kategorie"));
				fields.put("Bereich", getAreaByID(getAreaIDByTBZ_ID(tbz_id)));
				fields.put("Institution", getInstByID(rst.getInt("InstitutionsID")));
				fields.put("Status", rst.getString("Status"));
				fields.put("Thema", getTopicByID(getTopicIDByTBZ_ID(tbz_id)));
				fields.put("Inhalt", rst.getString("Inhalt"));
				fields.put("Ueberschrift", rst.getString("Überschrift"));
				int todoId = rst.getInt("ToDoID");
				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", todoId)));
				fields.put("Beteiligte", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_involved_personnel", "todoID", todoId)));

				if (rst.getBoolean("WiedervorlageGesetzt"))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					fields.put("Wiedervorlagedatum", sdf.format(rst.getDate("Wiedervorlagedatum")));
				}
				else
				{
					fields.put("Wiedervorlagedatum", "kein");
				}

				String sql2 = "SELECT * FROM Sitzungsdaten INNER JOIN Sitzungsart ON Sitzungsdaten.SitzungsartID = Sitzungsart.SitzungsartID WHERE SitzungsdatenID = " + rst.getInt("SitzungsID");
				ResultSet rst2 = stmt2.executeQuery(sql2);
				rst2.next();

				fields.put("SitzOrt", rst2.getString("Ort"));
				java.util.Date md = rst2.getDate("Datum");

				if (md != null)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					fields.put("SitzDatum", sdf.format(md));
				}
				else
				{
					fields.put("SitzDatum", "kein");
				}

				fields.put("SitzName", rst2.getString("Name"));
				data.add(fields);
				rst2.close();
			}

			rst.close();
			stmt.close();
			stmt2.close();

		}
		catch (Exception ex)
		{
			Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return data;
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
				Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
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
			Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		return extractedIds;
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
				Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
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
				Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
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
	
	public int getMeetingTypeIDByName(String meetingTypeName)
	{
		int meetingTypeId = -1;
		Connection con = DatabaseTodoConnect.openDB();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsart WHERE Name='" + meetingTypeName + "'";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				meetingTypeId = rst.getInt("SitzungsartID");
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(OpenTodoReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return meetingTypeId;
	}
}
