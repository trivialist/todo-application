package todo.gui;
/*
 * ParticipantsGUI.java
 *
 * Created on 22. Januar 2007, 01:00
 */
import todo.dbcon.DB_ToDo_Connect;
import todo.tablemodel.EmployeeTableModel;
import todo.tablemodel.ParticipantsTableModel;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;
import java.lang.String.*;
/**
 *
 * @author  Marcus Hertel
 */
public class ParticipantsGUI extends javax.swing.JFrame {
    
    private static Vector participants = new Vector();
    private static String others;
    private static int meetingID;
    private Connection con; 
    
    /** Creates new form ParticipantsGUI */
    public ParticipantsGUI(Vector participants, String others, int meetingID) {
        this.meetingID = meetingID;
        this.participants = participants;
        this.others = others;
        initComponents();
        jTextAreaOtherParticipants.setText(others);
        jTable1.setAutoCreateRowSorter(true);
        jTable2.setAutoCreateRowSorter(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPaneEmployees = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPaneParticipants = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonAddParticipant = new javax.swing.JButton();
        jButtonRemoveParticipant = new javax.swing.JButton();
        jButtonSaveAndExit = new javax.swing.JButton();
        jScrollPaneOtherParticipants = new javax.swing.JScrollPane();
        jTextAreaOtherParticipants = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(550, 400));
        jPanel1.setPreferredSize(new java.awt.Dimension(520, 460));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPaneEmployees.setViewportView(jTable1);

        jPanel1.add(jScrollPaneEmployees, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 230));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPaneParticipants.setViewportView(jTable2);

        jPanel1.add(jScrollPaneParticipants, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 210, 230));

        jLabel1.setText("Alle Mitarbeiter");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel2.setText("Teilnehmer");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        jLabel3.setText("Sonstige Teilnehmer");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jButtonAddParticipant.setText(">");
        jButtonAddParticipant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParticipantActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonAddParticipant, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 50, -1));

        jButtonRemoveParticipant.setText("<");
        jButtonRemoveParticipant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveParticipantActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonRemoveParticipant, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 50, -1));

        jButtonSaveAndExit.setText("Fertig");
        jButtonSaveAndExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAndExitActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSaveAndExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 420, 80, -1));

        jTextAreaOtherParticipants.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPaneOtherParticipants.setViewportView(jTextAreaOtherParticipants);

        jPanel1.add(jScrollPaneOtherParticipants, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 480, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     *
     * @param evt
     */
    private void jButtonSaveAndExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAndExitActionPerformed
        StringBuffer dbStringParticipants = new StringBuffer("");
        Enumeration partEnum = participants.elements ();
        while (partEnum.hasMoreElements ()) {
            int employeeID= Integer.valueOf(partEnum.nextElement().toString()).intValue();
            dbStringParticipants.append("," + employeeID);
        }
        if(jTextAreaOtherParticipants.getText().equals("")) {
            others="";
        }
        else {
            others = jTextAreaOtherParticipants.getText();
        }
        saveParticipants(meetingID, dbStringParticipants.toString(), others);
        dispose();
    }//GEN-LAST:event_jButtonSaveAndExitActionPerformed

    private void jButtonRemoveParticipantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveParticipantActionPerformed
        if(jTable2.getSelectedRow() != -1) {
            Object empID = jTable2.getValueAt(jTable2.getSelectedRow(), 0);
            Integer temp = new Integer(String.valueOf(empID));
            int ID = temp.intValue();
            if(participants.contains(ID)) {
                participants.removeElement(ID);
            }
        }
        jTable2.setModel(new ParticipantsTableModel(participants, meetingID));
    }//GEN-LAST:event_jButtonRemoveParticipantActionPerformed

    private void jButtonAddParticipantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParticipantActionPerformed
        if(jTable1.getSelectedRow() != -1) {
            Object empID = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            Integer temp = new Integer(String.valueOf(empID));
            int ID = temp.intValue();
            if(!participants.contains(ID)) {
                participants.add(ID);
            }
        }
        jTable2.setModel(new ParticipantsTableModel(participants, meetingID));
    }//GEN-LAST:event_jButtonAddParticipantActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if(evt.WINDOW_GAINED_FOCUS != 1) {
            jTable1.setModel(new EmployeeTableModel());
            jTable2.setModel(new ParticipantsTableModel(participants, meetingID));
        }
    }//GEN-LAST:event_formWindowGainedFocus
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParticipantsGUI(participants, others, meetingID).setVisible(true);
            }
        });
    }*/

    /**
     * Diese Funktion hinterlegt die Daten der Teilnehmer einer Sitzung in
     * der Tabelle Sitzungsdaten(Teilnehmer, Sonstige) der Datenbank.
     * Der String part enth�lt die Mitarbeiter-ID's der Mitarbeiter DB.
     * Der String otherPart enth�lt sonstige Teilnehmer als Volltext.
     *
     * @param meetingID
     * @param part
     * @param othPart
     */
    public void saveParticipants(int meetingID, String part, String othPart) {
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        
        try {
            Statement stmt = con.createStatement();
            String sql = "UPDATE Sitzungsdaten SET Teilnehmer='" + part 
                        + "', Sonstige='" + othPart 
                        + "' WHERE SitzungsdatenID=" + meetingID;
                stmt.executeUpdate(sql);
                stmt.close();
            } catch(Exception e) {
                System.out.println(e.toString()); 
                System.exit(1);
            }
            dbCon.closeDB(con);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddParticipant;
    private javax.swing.JButton jButtonRemoveParticipant;
    private javax.swing.JButton jButtonSaveAndExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneEmployees;
    private javax.swing.JScrollPane jScrollPaneOtherParticipants;
    private javax.swing.JScrollPane jScrollPaneParticipants;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextAreaOtherParticipants;
    // End of variables declaration//GEN-END:variables
    
}
