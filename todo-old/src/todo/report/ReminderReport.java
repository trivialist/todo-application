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
import todo.entity.Meeting;
import todo.entity.Todo;
import todo.gui.MainGui;
import todo.util.GlobalError;

public class ReminderReport
{
	public void createReport(String reportStatus, Date reportDate)
	{
		if (reportStatus.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen des Reports. Sie haben keinen Status ausgewählt", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			String reportSource = MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "WV_Liste.jrxml";
			Calendar cal = Calendar.getInstance();
			Todo td = new Todo();

			HashMap<String, Object> params = new HashMap<String, Object>();

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			params.put("Datum", sdf.format(cal.getTime()));
			params.put("Wiedervorlagedatum", sdf.format(reportDate));
			params.put("IMAGE", MainGui.applicationProperties.getProperty("JasperReportsTemplatePath") + "img\\logo_konzepte.gif");

			td.setReDate(reportDate);
			java.sql.Date date = new java.sql.Date(reportDate.getTime());
			ArrayList<HashMap> rdl = loadReDateData(date, getFinStatusIDByName(reportStatus));
			JRMapCollectionDataSource dataSet = new JRMapCollectionDataSource(rdl);

			if (!rdl.isEmpty())
			{
				try
				{
					JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSet);

					JasperViewer.viewReport(jasperPrint, false);
				}
				catch (JRException ex)
				{
					Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Es wurden keine Elemente gefunden!");
			}
		}
	}
	
	private ArrayList loadReDateData(Date date, int statusId)
	{
		ArrayList<HashMap> reDateData = new ArrayList<HashMap>();
		int tbz_id = -1;
		Connection con = DatabaseTodoConnect.openDB();
		
		try
		{
			PreparedStatement pStmt;
			
			if (statusId == -1)
			{
				pStmt = con.prepareStatement("SELECT * FROM Protokollelement WHERE "
											 + "Wiedervorlagedatum < ? AND Wiedervorlagedatum > ? AND WiedervorlageGesetzt = true AND Geloescht = false "
											 + "ORDER BY Wiedervorlagedatum DESC");
			}
			else
			{
				pStmt = con.prepareStatement("SELECT * FROM Protokollelement WHERE "
											 + "Wiedervorlagedatum < ? AND Wiedervorlagedatum > ? AND WiedervorlageGesetzt = true AND Geloescht = false "
											 + "AND StatusID = ? "
											 + "ORDER BY Wiedervorlagedatum DESC");
				pStmt.setInt(3, statusId);
			}
			
			pStmt.setDate(1, new java.sql.Date(date.getTime()));
			pStmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
			ResultSet rst = pStmt.executeQuery();

			while (rst.next())
			{
				HashMap<String, String> fields = new HashMap<String, String>();
				Meeting m = getMeetingDataByID(rst.getInt("SitzungsID"));
				tbz_id = rst.getInt("TBZuordnung_ID");
				fields.put("Kategorie", getCatByID(rst.getInt("KategorieID")));
				fields.put("Bereich", getAreaByID(getAreaIDByTBZ_ID(tbz_id)));
				fields.put("Institution", getInstByID(rst.getInt("InstitutionsID")));
				fields.put("Status", getStatByID(rst.getInt("StatusID")));
				fields.put("Thema", getTopicByID(getTopicIDByTBZ_ID(tbz_id)));
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

				int todoId = rst.getInt("ToDoID");
				fields.put("Verantwortliche", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_responsible_personnel", "todoID", todoId)));
				fields.put("Beteiligte", getNameAndLastNameByID(getPersonnelIdsFromItemId("todo_involved_personnel", "todoID", todoId)));
				fields.put("SitzOrt", m.getPlace());
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				fields.put("SitzDatum", sdf.format(m.getDate()));
				fields.put("SitzName", m.getMeetingType());
				reDateData.add(fields);
			}

			rst.close();
			pStmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return reDateData;
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
				Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return name;
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return mType;
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
			Logger.getLogger(ReminderReport.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DatabaseTodoConnect.closeDB(con);
		return statID;
	}
}
