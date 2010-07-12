/*
 * MeetingGUI.java
 *
 * Created on 6. Januar 2007, 21:48
 */
package todo.gui;

import todo.subgui.MeetingSearchSubGUI;
import todo.tablemodel.DateFormater;
import java.sql.Connection;
import java.sql.Date;
import todo.subgui.MeetingSubGUI;
import todo.dialog.DeleteMeetingDialog;
import todo.tablemodel.MeetingTableModel;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import todo.dbcon.DB_ToDo_Connect;

/**
 *
 * @author  Marcus Hertel
 */
public class MeetingGUI extends javax.swing.JFrame
{

	private static MeetingGUI meetGUI = new MeetingGUI();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String keyword = "";
	private String field = "";

	/** Creates new form MeetingGUI */
	public MeetingGUI()
	{
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonDeleteMeeting = new javax.swing.JButton();
        jButtonChooseMeeting = new javax.swing.JButton();
        jButtonEditMeeting = new javax.swing.JButton();
        jButtonNewMeeting = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Verwaltung der eingetragenen Sitzungen");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonDeleteMeeting.setText("Sitzung l�schen");
        jButtonDeleteMeeting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteMeetingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDeleteMeeting, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, 150, -1));

        jButtonChooseMeeting.setText("Sitzung w�hlen");
        jButtonChooseMeeting.setToolTipText("Als aktive Sitzung festlegen");
        jButtonChooseMeeting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseMeetingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonChooseMeeting, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, -1));

        jButtonEditMeeting.setText("Sitzung bearbeiten");
        jButtonEditMeeting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditMeetingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonEditMeeting, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 140, -1));

        jButtonNewMeeting.setText("Neue Sitzung");
        jButtonNewMeeting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewMeetingActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonNewMeeting, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 130, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 600, 440));

        jButton1.setText("Sitzung kopieren");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 140, -1));

        jButton2.setText("Sitzung suchen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 130, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 640, 550));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
		if (evt.WINDOW_GAINED_FOCUS != 1)
		{
			jTable1.setModel(new MeetingTableModel(keyword, field));
			jTable1.setAutoCreateRowSorter(true);
		}
    }//GEN-LAST:event_formWindowGainedFocus

    private void jButtonDeleteMeetingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteMeetingActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			Object meetType = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
			Object date = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
			Object meetingID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
			Object place = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
			Integer temp = new Integer(String.valueOf(meetingID));
			int meetID = temp.intValue();
			if (meetType != null)
			{
				DeleteMeetingDialog delMeet = new DeleteMeetingDialog(meetGUI, true,
						meetID, String.valueOf(meetType), String.valueOf(date),
						String.valueOf(place));
				delMeet.setVisible(true);
			}
		}
    }//GEN-LAST:event_jButtonDeleteMeetingActionPerformed

    private void jButtonEditMeetingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditMeetingActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			Object meetType = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
			Object date = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
			Object meetingID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
			Integer temp = new Integer(String.valueOf(meetingID));
			int meetID = temp.intValue();
			if (meetType != null)
			{
				MeetingSubGUI newMeet = new MeetingSubGUI(1, meetID,
						String.valueOf(meetType), String.valueOf(date), "");
				newMeet.setVisible(true);
			}
		}
    }//GEN-LAST:event_jButtonEditMeetingActionPerformed

    private void jButtonNewMeetingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewMeetingActionPerformed
		String meetingType = "";
		String date = "";
		int meetingID = 0;
		String otherPart = "";
		MeetingSubGUI newMeet = new MeetingSubGUI(0, meetingID, meetingType, date, otherPart);
		newMeet.setVisible(true);
    }//GEN-LAST:event_jButtonNewMeetingActionPerformed

    private void jButtonChooseMeetingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseMeetingActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			Object meetingID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
			Integer temp = new Integer(String.valueOf(meetingID));
			int mID = temp.intValue();
			Object place = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
			Object meetType = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
			java.sql.Date date = (Date) ((DateFormater) jTable1.getValueAt(jTable1.getSelectedRow(), 0)).getDate();
			MainGUI.setActMeeting(mID, String.valueOf(place), date, String.valueOf(meetType));
		}
		dispose();
    }//GEN-LAST:event_jButtonChooseMeetingActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			int meetingID = Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), -1).toString());
			int insertId = -1;
			ResultSet results = null;

			DB_ToDo_Connect.openDB();
			Connection con = DB_ToDo_Connect.getCon();

			try
			{
				con.setAutoCommit(false);

				Statement id = con.createStatement();
				ResultSet rst = id.executeQuery("SELECT MAX(SitzungsdatenID) FROM Sitzungsdaten");
				rst.next();
				insertId = rst.getInt(1) + 1;
				id.close();

				Statement stmt = con.createStatement();
				String sql = "INSERT INTO Sitzungsdaten (SitzungsdatenID, Datum, Ort, Tagesordnung, " +
						"SitzungsartID, Protokollant, Teilnehmer, Sonstige) SELECT " + insertId +
						" AS SitzungsdatenID, Datum, Ort, Tagesordnung, SitzungsartID, Protokollant, " +
						"Teilnehmer, Sonstige FROM Sitzungsdaten WHERE SitzungsdatenID = " + meetingID;
				stmt.executeUpdate(sql);
				stmt.close();

				con.commit();
				con.setAutoCommit(true);

				Statement data = con.createStatement();
				results = data.executeQuery("SELECT * FROM Sitzungsart INNER JOIN " +
						"Sitzungsdaten ON Sitzungsart.SitzungsartID = Sitzungsdaten.SitzungsartID " +
						"WHERE Sitzungsdaten.SitzungsdatenID=" + insertId);
				results.next();
				MainGUI.setActMeeting(insertId, results.getString("Ort"), results.getDate("Datum"), results.getString("Name"));
				data.close();
			} catch (Exception ex)
			{
				Logger.getLogger(MeetingGUI.class.getName()).log(Level.SEVERE, null, ex);
			}

			DB_ToDo_Connect.closeDB(con);
			dispose();
		}
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
		MeetingSearchSubGUI search = new MeetingSearchSubGUI(this);
		search.setLocation(jButton2.getX() - 40, jButton2.getY() + 60);
		search.setVisible(true);
	}//GEN-LAST:event_jButton2ActionPerformed

	public void updateSearch(String keyword, String field)
	{
		this.keyword = keyword;
		this.field = field;
		jTable1.setModel(new MeetingTableModel(keyword, field));
		jTable1.setAutoCreateRowSorter(true);
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonChooseMeeting;
    private javax.swing.JButton jButtonDeleteMeeting;
    private javax.swing.JButton jButtonEditMeeting;
    private javax.swing.JButton jButtonNewMeeting;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
