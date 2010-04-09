/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.joc;

import com.moyosoft.connector.com.*;
import com.moyosoft.connector.exception.*;
import com.moyosoft.connector.ms.outlook.*;
import com.moyosoft.connector.ms.outlook.folder.*;
import com.moyosoft.connector.ms.outlook.task.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import todo.subgui.MeetingSubGUI;

/**
 *
 * @author hertel
 *
 */
public class TaskRequest
{

	private String header = "";
	private String area = "";
	private String topic = "";
	private String content = "";
	private Date reDate;
	private Vector respVec;

	/**
	 * Standard-Konstruktor für Aufgaben-Anfrage
	 *
	 * @param header
	 * @param area
	 * @param topic
	 * @param content
	 * @param reDate
	 * @param prot
	 * @param respVec
	 */
	public TaskRequest(String header, String area, String topic, String content, Date reDate, Vector respVec)
	{
		this.header = header;
		this.area = area;
		this.topic = topic;
		this.content = content;
		this.reDate = reDate;
		this.respVec = respVec;
	}

	public void create()
	{
		try
		{
			// Aufgabenobjekt erstellen
			Outlook outlookApplication = new Outlook();
			OutlookFolder tasks = outlookApplication.getDefaultFolder(FolderType.TASKS);
			OutlookTask task = new OutlookTask(tasks);

			//Eigenschaften der Aufgabe festlegen
			//Betreff
			task.setSubject("PDB Aufgabe" + header);
			//Inhalt
			task.setBody("Bereich: " + area + ", Thema: " + topic + "text\n\n" + "Inhalt: " + content);
			//Fällig am
			task.setDueDate(reDate);
			//Empänger(Verantwortlicher) als Mail und als Text
			task.assign();

			Enumeration enumResp = respVec.elements();
			while (enumResp.hasMoreElements())
			{
				try
				{

					//Empfänger(Verantwortliche/r) festlegen
					task.getRecipients().add(enumResp.nextElement().toString());

				} catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
							"Beim Erstellen der Empfänger ist ein Fehler aufgetreten.\n" + "Bitte überprüfen Sie die E-Mail Adressen der Empfänger", "Fehler", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}

			task.display(true);

			//freigeben der Anwendung
			outlookApplication.dispose();

		} catch (ComponentObjectModelException ex)
		{
			JOptionPane.showMessageDialog(null, "COM-Fehler: " + ex.getMessage());
			Logger.getLogger(TaskRequest.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		} catch (LibraryNotFoundException ex)
		{
			// Wenn dieser Fehler auftritt, fehlt die Datei 'moyocore.dll' im Java-Lib-Pfad
			JOptionPane.showMessageDialog(null, "Die Java Outlook Bibliothek moyocore.dll konnte nicht gefunden werden.");
			ex.printStackTrace();
			Logger.getLogger(TaskRequest.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		}
	}

	/**
	 * Prüfen ob alle benötigten Felder ausgefüllt wurden
	 *
	 * @return
	 */
	public boolean isSet()
	{
		if (header.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
					"Betreff konnte nicht erstellt werden", "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (area.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
					"Bereich konnte nicht erstellt werden", "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (topic.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
					"Thema konnte nicht erstellt werden", "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (content.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
					"Inhalt konnte nicht erstellt werden", "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (reDate == null)
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
					"WV-Datum konnte nicht erstellt werden", "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (respVec == null)
		{
			JOptionPane.showMessageDialog(null, "Fehler beim Erstellen der Aufgabe. " +
					"Verantwortliche/r konnte/n nicht erstellt werden", "Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
		{
			return true;
		}
	}
}
