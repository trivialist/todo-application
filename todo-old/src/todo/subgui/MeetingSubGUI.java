/*
 * MeetingSubGUI.java
 *
 * Created on 6. Januar 2007, 17:14
 */
package todo.subgui;

import todo.gui.GlobalError;
import todo.core.Employee;
import todo.core.Meeting;
import todo.core.MeetingType;
import todo.dbcon.DB_ToDo_Connect;
import todo.dbcon.DB_Mitarbeiter_Connect;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import todo.gui.MainGUI;
import todo.tablemodel.EmployeeTreeModel;
import todo.tablemodel.EmployeeTreeModel.Group;
import todo.tablemodel.EmployeeTreeModel.NameLeaf;
import todo.tablemodel.ParticipantsTableModel;

/**
 *
 * @author  Marcus Hertel
 */
public class MeetingSubGUI extends javax.swing.JFrame
{
	private int status = 0;
	private int meetingID;
	private Meeting meet = new Meeting();
	private static Connection con;
	private static Connection con2;
	private ArrayList<Integer> participants = new ArrayList<Integer>();
	private Calendar cal = Calendar.getInstance();
	private boolean flagParticipants = false;  //true, falls Teilnehmer hinzugef�gt wurden
	// und somit schon Sitzung angelegt wurde.

	/**
	 * Creates new form MeetingSubGUI
	 */
	public MeetingSubGUI()
	{
		initComponents();
		setLocationRelativeTo(null);
	}

	public MeetingSubGUI(int status, int meetingID, String meetingType, String date, String otherPart)
	{
		this.status = status;
		this.meetingID = meetingID;
		initComponents();
		setLocationRelativeTo(null);
		if (status == 0)
		{
			//neue Sitzung anlegen
			newMeetingInit();
		}
		if (status == 1)
		{
			//vorhandene Sitzung bearbeiten
			editMeetingInit();
			jTable2.setModel(new ParticipantsTableModel(participants, meetingID));
		}

		EmployeeTreeModel tm = (EmployeeTreeModel) jTreeParticipiants.getModel();
		EmployeeTreeModel.Group etm = (Group) tm.getRoot();
		ArrayList<Object> childs = etm.getChilds();
		for (Object child : childs)
		{
			if (child.toString().equals("Verwaltung"))
			{
				Object path[] =
				{
					tm.getRoot(), child
				};
				TreePath tp = new TreePath(path);
				jTreeParticipiants.expandPath(tp);
				break;
			}
		}

		jTextAreaOtherParticipants.setText(meet.getOtherPaticipants());
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBoxMeetingType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPlace = new javax.swing.JTextField();
        jComboBoxProt = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jButtonSaveAndExit = new javax.swing.JButton();
        jCalendarComboBoxDate = new de.wannawork.jcalendar.JCalendarComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPaneParticipants = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButtonAddParticipant = new javax.swing.JButton();
        jButtonRemoveParticipant = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeParticipiants = new javax.swing.JTree();
        jTextAreaOtherParticipants = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sitzung verwalten");
        setMinimumSize(new java.awt.Dimension(520, 530));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Datum");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, -1));
        getContentPane().add(jComboBoxMeetingType, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 240, -1));

        jLabel2.setText("Ort");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        getContentPane().add(jTextFieldPlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 240, -1));
        getContentPane().add(jComboBoxProt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 240, -1));

        jLabel4.setText("Protokollant");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 60, -1));

        jButtonSaveAndExit.setText("Speichern und schliessen");
        jButtonSaveAndExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAndExitActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSaveAndExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 470, -1, -1));

        jCalendarComboBoxDate.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.shadow")));
        jCalendarComboBoxDate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCalendarComboBoxDateStateChanged(evt);
            }
        });
        getContentPane().add(jCalendarComboBoxDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 20));

        jLabel5.setText("Sitzungsart");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        jLabel6.setText("Alle Mitarbeiter");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jLabel7.setText("Teilnehmer");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPaneParticipants.setViewportView(jTable2);

        getContentPane().add(jScrollPaneParticipants, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 210, 230));

        jButtonAddParticipant.setText(">");
        jButtonAddParticipant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParticipantActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonAddParticipant, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 50, -1));

        jButtonRemoveParticipant.setText("<");
        jButtonRemoveParticipant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveParticipantActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonRemoveParticipant, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 50, -1));

        jLabel8.setText("Sonstige Teilnehmer");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, -1));

        jTreeParticipiants.setModel(new EmployeeTreeModel());
        jTreeParticipiants.setRootVisible(false);
        jTreeParticipiants.setShowsRootHandles(true);
        jScrollPane1.setViewportView(jTreeParticipiants);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 210, 230));

        jTextAreaOtherParticipants.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(jTextAreaOtherParticipants, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 490, 70));

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * �ffnet ParticipantsGUI um Teilnehmer zu bearbeiten
	 *
	 * @param evt
	 */
    private void jButtonSaveAndExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAndExitActionPerformed

		switch (status)
		{
			case 0:         // status=0, neue Sitzung anlegen
				if (flagParticipants)
				{   //Sitzung ist schon angelegt
					getMeetingID();
					editMeeting();
					setVisible(false);
					break;
				}
				else
				{
					newMeeting();
					setVisible(false);
					break;
				}
			case 1:         // status=1, vorhandene Sitzung bearbeiten
				editMeeting();
				setVisible(false);
				break;
			default:
				JOptionPane.showMessageDialog(rootPane, "Fehler bei Ausf�hrung des Befehls!");
		}

		getMeetingID();
		String others = jTextAreaOtherParticipants.getText();
		saveParticipants(meetingID, participants, others);
    }//GEN-LAST:event_jButtonSaveAndExitActionPerformed

	public void saveParticipants(int meetingID, ArrayList<Integer> part, String othPart)
	{
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "UPDATE Sitzungsdaten SET Sonstige = '" + othPart + "' WHERE SitzungsdatenID = " + meetingID;
			stmt.executeUpdate(sql);
			stmt.close();

			MainGUI.updateRelations(con, "meeting_attendee_personnel", "meetingID", participants, meetingID);
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}

    private void jCalendarComboBoxDateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCalendarComboBoxDateStateChanged
		if (evt.getSource() == jCalendarComboBoxDate)
		{
			cal.set(jCalendarComboBoxDate.getCalendar().get(Calendar.YEAR),
					jCalendarComboBoxDate.getCalendar().get(Calendar.MONTH) + 1,
					jCalendarComboBoxDate.getCalendar().get(Calendar.DAY_OF_MONTH));
		}
}//GEN-LAST:event_jCalendarComboBoxDateStateChanged

	private void jButtonAddParticipantActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonAddParticipantActionPerformed
	{//GEN-HEADEREND:event_jButtonAddParticipantActionPerformed
		TreePath path = jTreeParticipiants.getSelectionPath();
		Object selectedObject = path.getPath()[path.getPathCount() - 1];

		if (selectedObject instanceof NameLeaf)
		{
			NameLeaf selectedName = (NameLeaf) selectedObject;
			if (!participants.contains(selectedName.getId()))
			{
				participants.add(selectedName.getId());
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Bitte w�hlen Sie einen Namen aus und nicht das �bergeordnete Gruppenelement.");
		}
		jTable2.setModel(new ParticipantsTableModel(participants, meetingID));
}//GEN-LAST:event_jButtonAddParticipantActionPerformed

	private void jButtonRemoveParticipantActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonRemoveParticipantActionPerformed
	{//GEN-HEADEREND:event_jButtonRemoveParticipantActionPerformed
		if (jTable2.getSelectedRow() != -1)
		{
			Object empID = jTable2.getValueAt(jTable2.getSelectedRow(), -1);
			Integer temp = new Integer(String.valueOf(empID));
			int ID = temp.intValue();
			if (participants.contains(ID))
			{
				participants.remove((Integer) ID);
			}
		}

		jTable2.setModel(new ParticipantsTableModel(participants, meetingID));
}//GEN-LAST:event_jButtonRemoveParticipantActionPerformed

	public void newMeetingInit()
	{
		jComboBoxMeetingType.addItem("Bitte w�hlen...");
		jComboBoxProt.addItem("Bitte w�hlen...");
		ArrayList<MeetingType> mt = getAllMeetingTypes();
		ArrayList<Employee> emp = getAllEmployees();
		for (int i = 0; i < mt.size(); i++)
		{
			jComboBoxMeetingType.addItem(mt.get(i).getMeetingType());
		}
		for (int i = 0; i < emp.size(); i++)
		{
			jComboBoxProt.addItem(emp.get(i).getLastName() + ", " + emp.get(i).getName());
		}
	}

	/**
	 * Alle Ben�tigten Daten zum Bearbeiten der bereits vorhandenen Sitzung
	 * werden geladen
	 */
	public void editMeetingInit()
	{
		ArrayList<MeetingType> mt = getAllMeetingTypes();
		ArrayList<Employee> emp = getAllEmployees();
		for (int i = 0; i < mt.size(); i++)
		{
			jComboBoxMeetingType.addItem(mt.get(i).getMeetingType());
		}
		for (int i = 0; i < emp.size(); i++)
		{
			jComboBoxProt.addItem(emp.get(i).getLastName() + ", " + emp.get(i).getName());
		}
		refreshMeetingData();
		cal.setTime(meet.getDate());
		jCalendarComboBoxDate.setCalendar(cal);
		jTextFieldPlace.setText(meet.getPlace());
		jComboBoxProt.setSelectedItem(getNameOfProt(meet.getProt()));
		jComboBoxMeetingType.setSelectedItem(meet.getMeetingType());
	}

	/*
	 *
	 */
	public void newMeeting()
	{
		meet.setDate(jCalendarComboBoxDate.getCalendar().getTime());
		meet.setMeetingType(String.valueOf(jComboBoxMeetingType.getSelectedItem()));
		meet.setMeetingTypeID(getMeetingTypeIDByName(meet.getMeetingType()));
		meet.setPlace(jTextFieldPlace.getText());
		if (!String.valueOf(jComboBoxProt.getSelectedItem()).equals("Bitte w�hlen..."))
		{
			meet.setProt(getProtIDByName(String.valueOf(jComboBoxProt.getSelectedItem())));
		}
		else
		{
			meet.setProt(1);
		}

		if (!meet.getMeetingType().equals(""))
		{
			DB_ToDo_Connect.openDB();
			con = DB_ToDo_Connect.getCon();
			try
			{
				Statement stmt = con.createStatement();
				//Verantwortliche
				java.sql.Date dat = new java.sql.Date(meet.getDate().getTime());
				String sql = "INSERT INTO Sitzungsdaten (Datum, SitzungsartID, "
							 + "Ort, Protokollant, Geloescht) VALUES ('" + dat + "', "
							 + meet.getMeetingTypeID() + ", '" + meet.getPlace() + "', '"
							 + meet.getProt() + "', false)";
				stmt.executeUpdate(sql);
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}
			DB_ToDo_Connect.closeDB(con);
		}
		else
		{
			JOptionPane.showMessageDialog(rootPane, "Die Felder Datum, Sitzungsart und Ort m�ssen ausgef�llt werden!");
		}
	}

	/**
	 * - �nderung von Daten einer Sitzung -
	 * Alle angegebenen Daten der bereits vorhandenen Sitzung werden in die
	 * Tabelle Sitzungsdaten der Datenbank hinterlegt
	 */
	public void editMeeting()
	{
		meet.setDate(jCalendarComboBoxDate.getCalendar().getTime());
		meet.setMeetingType(String.valueOf(jComboBoxMeetingType.getSelectedItem()));
		meet.setMeetingTypeID(getMeetingTypeIDByName(meet.getMeetingType()));
		meet.setPlace(jTextFieldPlace.getText());
		if (!String.valueOf(jComboBoxProt.getSelectedItem()).equals("Bitte w�hlen..."))
		{
			meet.setProt(getProtIDByName(String.valueOf(jComboBoxProt.getSelectedItem())));
		}
		else
		{
			meet.setProt(1);
		}

		if (!meet.getMeetingType().equals(""))
		{
			DB_ToDo_Connect.openDB();
			con = DB_ToDo_Connect.getCon();
			try
			{
				Statement stmt = con.createStatement();
				//Verantwortliche
				java.sql.Date dat = new java.sql.Date(meet.getDate().getTime());
				String sql = "UPDATE Sitzungsdaten SET Datum = '" + dat + "', SitzungsartID = " + meet.getMeetingTypeID() + ", Ort = '" + meet.getPlace() + "', Protokollant = " + meet.getProt() + " WHERE SitzungsdatenID = " + meetingID;
				stmt.executeUpdate(sql);
				stmt.close();
			}
			catch (Exception ex)
			{
				Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
				GlobalError.showErrorAndExit();
			}
			DB_ToDo_Connect.closeDB(con);
		}
		else
		{
			JOptionPane.showMessageDialog(rootPane, "Die Felder Datum, Sitzungsart und Ort m�ssen ausgef�llt werden!");
		}
	}

	/*
	 * ermittelt den Namen des Protokollanten bez�glich dessen ID
	 */
	public String getNameOfProt(int protID)
	{
		String protName = "";
		DB_Mitarbeiter_Connect.openDB();
		con2 = DB_Mitarbeiter_Connect.getCon();

		try
		{
			Statement stmt = con2.createStatement();
			String sql = "SELECT * FROM Stammdaten WHERE Personalnummer = " + protID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				protName = rst.getString("Nachname") + ", " + rst.getString("Vorname");
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		return protName;
	}

	/*
	 *  ermittelt die Namen aller Mitarbeiter zur Auswahl des Protokollanten
	 */
	public ArrayList getAllEmployees()
	{
		ArrayList<Employee> employeeObjects = new ArrayList<Employee>();
		DB_Mitarbeiter_Connect.openDB();
		con = DB_Mitarbeiter_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Stammdaten ORDER BY Nachname ASC";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				employeeObjects.add(new Employee((rst.getInt("Personalnummer")),
												 rst.getString("Vorname"), rst.getString("Nachname")));
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		return employeeObjects;
	}

	/*
	 * ermittlet alle Sitzungsartenobjekte zur Auswahl
	 */
	public ArrayList getAllMeetingTypes()
	{
		ArrayList<MeetingType> meetingTypeObjects = new ArrayList<MeetingType>();
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsart ORDER BY Name ASC";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				meetingTypeObjects.add(new MeetingType(rst.getInt("SitzungsartID"),
													   rst.getString("Name")));
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		return meetingTypeObjects;
	}

	public void getMeetingData()
	{
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsdaten WHERE SitzungsdatenID = " + meetingID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				meet.setDate(rst.getDate("Datum"));
				meet.setPlace(rst.getString("Ort"));
				meet.setProt(rst.getInt("Protokollant"));
				meet.setOtherParticipants(rst.getString("Sonstige"));
				int meetingTypeID = rst.getInt("SitzungsartID");
				meet.setMeetingTypeID(meetingTypeID);
				String meetingType = "";
				Statement stmt2 = con.createStatement();
				String sql2 = "SELECT * FROM Sitzungsart WHERE SitzungsartID = "
							  + meetingTypeID;
				ResultSet rst2 = stmt2.executeQuery(sql2);

				while (rst2.next())
				{
					meet.setMeetingType(rst2.getString("Name"));
				}
				rst2.close();
				stmt2.close();

			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}

	public int getMeetingTypeIDByName(String meetingType)
	{
		int mtID = 0;
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsart WHERE Name = '" + meet.getMeetingType() + "'";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				mtID = rst.getInt("SitzungsartID");
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		return mtID;
	}

	public int getProtIDByName(String protName)
	{
		int prID = 0;
		DB_Mitarbeiter_Connect.openDB();
		con = DB_Mitarbeiter_Connect.getCon();

		String name = "";
		String lastName = "";

		String splitName[] = protName.split(",");
		lastName = splitName[0].trim();
		name = splitName[1].trim();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Stammdaten WHERE Vorname = '" + name + "' AND Nachname = '" + lastName + "'";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				prID = rst.getInt("Personalnummer");
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		return prID;
	}

	public void getAllParticipants()
	{
		// Vektor participants komplett leeren
		participants.clear();

		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT personnelID FROM meeting_attendee_personnel WHERE meetingID = " + meetingID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				participants.add(rst.getInt("personnelID"));
			}

			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		DB_ToDo_Connect.closeDB(con);
	}

	public void getMeetingID()
	{
		int mID = 0;
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Sitzungsdaten ORDER BY SitzungsdatenID ASC";
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				mID = rst.getInt("SitzungsdatenID");
			}
			rst.close();
			stmt.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(MeetingSubGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		meetingID = mID;
	}

	/**
	 * Aktualisierung der Daten einer Sitzung aus der Datenbank
	 * !notwendig da nach dem Schliessen von ParticipantsGUI sonst bei
	 * erneutem Aufrufen der Maske �ber "Teilnehmer verwalten", alte Daten
	 * angezeigt werden w�rden
	 */
	public void refreshMeetingData()
	{
		getMeetingData();
		getAllParticipants();
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddParticipant;
    private javax.swing.JButton jButtonRemoveParticipant;
    private javax.swing.JButton jButtonSaveAndExit;
    private de.wannawork.jcalendar.JCalendarComboBox jCalendarComboBoxDate;
    private javax.swing.JComboBox jComboBoxMeetingType;
    private javax.swing.JComboBox jComboBoxProt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneParticipants;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextAreaOtherParticipants;
    private javax.swing.JTextField jTextFieldPlace;
    private javax.swing.JTree jTreeParticipiants;
    // End of variables declaration//GEN-END:variables
}
