/*
 * CategorySubGUI.java
 *
 * Created on 30. Dezember 2006, 21:43
 */

package todo.subgui;

import todo.core.Category;
import todo.dbcon.DB_ToDo_Connect;
import java.sql.*;
/**
 *
 * @author  Marcus Hertel
 */
public class CategorySubGUI extends javax.swing.JFrame {
    
    private static int status;              //0=Neu; 1=Bearbeiten
    private static int catID;      
    private static String catName;     
    private static String catDescription;     
    private Category cat;          
    private Connection con; 
    /** Creates new form CategorySubGUI */
    public CategorySubGUI(int status, int catID, String catName, String catDescription) {
        this.status = status;
        this.catID = catID;
        this.catName = catName;
        this.catDescription = catDescription;
        initComponents();
        if(status == 0) {
            //Neue Kategorie soll angelegt werden
            newCategoryInit();
        }
        if(status == 1) {
            //Bestehende Kategorie soll bearbeitet werden
            editCategoryInit();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonSaveAndExit1 = new javax.swing.JButton();
        jLabelError = new javax.swing.JLabel();
        jTextFieldCatDescription = new javax.swing.JTextField();
        jTextFieldCatName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kategorien verwalten");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonSaveAndExit1.setText("Speichern und Schliessen");
        jButtonSaveAndExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAndExit1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSaveAndExit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, -1, -1));

        jLabelError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabelError, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 230, 40));
        jPanel1.add(jTextFieldCatDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 230, -1));
        jPanel1.add(jTextFieldCatName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 230, -1));

        jLabel3.setText("Kategorie");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 70, -1));

        jLabel4.setText("Beschreibung");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 70, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 380, 240));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveAndExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAndExit1ActionPerformed
        switch(status) {
            case 0:         // status=0, neue Kategorie anlegen
                newCategory();
                setVisible(false);
                break;
            case 1:         // status=1, vorhandene Kategorie bearbeiten
                editCategory();
                setVisible(false);
                break;
            default:
                jLabelError.setForeground(new java.awt.Color(255, 0, 0));
                jLabelError.setText("Fehler bei Ausf�hrung des Befehls!");
        }
    }//GEN-LAST:event_jButtonSaveAndExit1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CategorySubGUI(status, catID, catName, catDescription).setVisible(true);
            }
        });
    }
    
    public void newCategoryInit() {
        jLabelError.setForeground(new java.awt.Color(255, 0, 0));
        jLabelError.setText("Es m�ssen alle Felder ausgew�hlt werden");
    }
    
    public void editCategoryInit() {
        jTextFieldCatName.setText(catName);
        jTextFieldCatDescription.setText(catDescription);
    }
    
    public void newCategory() {
        catName = jTextFieldCatName.getText();
        catDescription = jTextFieldCatDescription.getText();
        if(catName != "") {
            DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
            dbCon.openDB();
            con = dbCon.getCon();
            try {
                Statement stmt = con.createStatement();
                String sql = "INSERT INTO Kategorie (Name, Beschreibung) VALUES " +
                        "('" + catName + "', '" + catDescription + "')";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch(Exception e) {
                System.out.println(e.toString()); 
                System.exit(1);
            }
            dbCon.closeDB(con);
        }
        else {
            jLabelError.setText("Das Feld Kategorie muss ausgef�llt werden!");
        }    
    }
    
    public void editCategory() {
        catName = jTextFieldCatName.getText();
        catDescription = jTextFieldCatDescription.getText();
        if(catName != "") {
            DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
            dbCon.openDB();
            con = dbCon.getCon();
        
            try {
                Statement stmt = con.createStatement();
                String sql = "UPDATE Kategorie SET Name='" + catName 
                            + "' WHERE KategorieID=" + catID;
                stmt.executeUpdate(sql);
                String sql2 = "UPDATE Kategorie SET Beschreibung='" + catDescription 
                            + "' WHERE KategorieID=" + catID;
                stmt.executeUpdate(sql2); 
                stmt.close();
            } 
            catch(Exception e) {
                System.out.println(e.toString()); 
                System.exit(1);
            }
            dbCon.closeDB(con);
        }
        else {
            jLabelError.setText("Das Feld Kategorie muss ausgef�llt werden!");
        }    
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSaveAndExit1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelError;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldCatDescription;
    private javax.swing.JTextField jTextFieldCatName;
    // End of variables declaration//GEN-END:variables
    
}