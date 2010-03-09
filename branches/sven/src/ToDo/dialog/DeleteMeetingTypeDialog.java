/*
 * DeleteMeetingTypeDialog.java
 *
 * Created on 28. Dezember 2006, 01:32
 */

package todo.dialog;

import todo.dbcon.DB_ToDo_Connect;
import java.sql.*;
/**
 *
 * @author  Gudrun
 */
public class DeleteMeetingTypeDialog extends javax.swing.JDialog {
    
    private static String meetingType = "";
    private static int meetingTypeID = 0;
    private Connection con;
    
    /** Creates new form DeleteMeetingTypeDialog */
    public DeleteMeetingTypeDialog(java.awt.Frame parent, boolean modal, 
                                    int meetingTypeID, String meetingType) {
        super(parent, modal);
        this.meetingTypeID = meetingTypeID;
        this.meetingType = meetingType;
        initComponents();
        setTitle("Sitzungsart " + meetingType + " l�schen?");
        jLabelMeetingType.setText(meetingType);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelMeetingType = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonCancel.setText("Nein");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, -1, -1));

        jButtonOk.setText("Ja");
        jButtonOk.setActionCommand("jbutton1");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, -1, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("    wirklich l�schen?");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 140, 20));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("M�chten Sie die Sitzungsart");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 190, 20));

        jLabelMeetingType.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabelMeetingType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMeetingType.setText("Fehler");
        jPanel1.add(jLabelMeetingType, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 160, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 300, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        deleteMeetingType(meetingTypeID);
        setVisible(false);
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    public void deleteMeetingType(int meetingTypeID) {
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        try {
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM Sitzungsart WHERE SitzungsartID=" + meetingTypeID;
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch(Exception e) {
            System.out.println(e.toString()); 
            System.exit(1); 
        }
        dbCon.closeDB(con);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelMeetingType;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
