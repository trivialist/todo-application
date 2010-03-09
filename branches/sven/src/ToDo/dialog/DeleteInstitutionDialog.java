/*
 * DeleteInstitutionDialog.java
 *
 * Created on 29. Dezember 2006, 18:11
 */

package todo.dialog;

import todo.dbcon.DB_ToDo_Connect;
import java.sql.*;
/**
 *
 * @author  Marcus Hertel
 */
public class DeleteInstitutionDialog extends javax.swing.JDialog {
    
    private static String institution = "";
    private static int institutionID = 0;
    private Connection con;
    /** Creates new form DeleteInstitutionDialog */
    public DeleteInstitutionDialog(java.awt.Frame parent, boolean modal,
                                    int institutionID, String institution) {
        super(parent, modal);
        this.institutionID = institutionID;
        this.institution = institution;
        initComponents();
        setTitle("Institution " + institution + " l�schen?");
        jLabelInstitution.setText(institution);
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
        jLabelInstitution = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonCancel.setText("Nein");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, -1, -1));

        jButtonOk.setText("Ja");
        jButtonOk.setActionCommand("jbutton1");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("    wirklich l�schen?");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 140, 20));

        jLabelInstitution.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabelInstitution.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelInstitution.setText("Fehler");
        jPanel1.add(jLabelInstitution, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 160, 20));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("M�chten Sie die Institution");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 280, 160));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        deleteInstitution(institutionID);
        setVisible(false);
    }//GEN-LAST:event_jButtonOkActionPerformed
       
    public void deleteInstitution(int institutionID) {
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        try {
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM Institution WHERE InstitutionID=" + institutionID;
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
    private javax.swing.JLabel jLabelInstitution;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
