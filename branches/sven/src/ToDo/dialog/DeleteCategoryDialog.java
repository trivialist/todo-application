/*
 * DeleteCategoryDialog.java
 *
 * Created on 30. Dezember 2006, 22:19
 */

package todo.dialog;

import todo.dbcon.DB_ToDo_Connect;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author  Marcus Hertel
 */
public class DeleteCategoryDialog extends javax.swing.JDialog {
    
    private static int catID = 0;
    private static String catName = "";
    private static String catDescription = "";  
    private Connection con;
    /** Creates new form DeleteCategoryDialog */
    public DeleteCategoryDialog(java.awt.Frame parent, boolean modal,
                        int catID, String catName, String catDescription) {
        super(parent, modal);
        this.catID = catID;
        this.catName = catName;
        this.catDescription = catDescription;
        initComponents();
        setTitle("Kategorie " + catName + " l�schen?");
        jLabelCategory.setText(catName);
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
        jLabelCategory = new javax.swing.JLabel();
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
        jPanel1.add(jButtonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));

        jButtonOk.setText("Ja");
        jButtonOk.setActionCommand("jbutton1");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("    wirklich l�schen?");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 140, 20));

        jLabelCategory.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabelCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCategory.setText("Fehler");
        jPanel1.add(jLabelCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 160, 20));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("M�chten Sie die Kategorie");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 260, 140));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        deleteCategory(catID);
        setVisible(false);
    }//GEN-LAST:event_jButtonOkActionPerformed
    
    public void deleteCategory(int catID) {
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        try {
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM Kategorie WHERE KategorieID=" + catID;
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch(Exception ex) {
            Logger.getLogger(DeleteCategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1); 
        }
        dbCon.closeDB(con);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelCategory;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
