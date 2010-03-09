/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TopicSubGUI.java
 *
 * Created on 29.05.2009, 14:43:06
 */

package todo.subgui;

import todo.core.Topic;
import todo.dbcon.DB_ToDo_Connect;
import java.sql.*;
/**
 *
 * @author hertel
 */
public class TopicSubGUI extends javax.swing.JFrame {

    private static int status;              //0=Neu; 1=Bearbeiten
    private static int topicID;
    private static String name;
    private static String description;
    private Topic topic;
    private Connection con;
    /** Creates new form TopicSubGUI */
    public TopicSubGUI(int status, int areaID, String areaName, String areaDescription) {
        this.status = status;
        this.topicID = topicID;
        this.name = name;
        this.description = description;
        initComponents();
        if(status == 0) {
            //Neuer Bereich soll angelegt werden
            newTopicInit();
        }
        if(status == 1) {
            //Bestehender Bereich soll bearbeitet werden
            editTopicInit();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonSaveAndExit = new javax.swing.JButton();
        jLabelError = new javax.swing.JLabel();
        jTextFieldTopicDescription = new javax.swing.JTextField();
        jTextFieldTopicName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thema erstellen/ bearbeiten");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonSaveAndExit.setText("Speichern und Schliessen");
        jButtonSaveAndExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAndExitActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSaveAndExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, -1, -1));

        jLabelError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabelError, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 230, 40));
        jPanel1.add(jTextFieldTopicDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 230, -1));
        jPanel1.add(jTextFieldTopicName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 230, -1));

        jLabel2.setText("Thema");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 70, -1));

        jLabel3.setText("Beschreibung");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 70, -1));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(0, 10, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 380, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 10, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(0, 30, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 30, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveAndExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAndExitActionPerformed
        switch(status) {
            case 0:         // status=0, neue Kategorie anlegen
                newTopic();
                setVisible(false);
                break;
            case 1:         // status=1, vorhandene Kategorie bearbeiten
                editTopic();
                setVisible(false);
                break;
            default:
                jLabelError.setForeground(new java.awt.Color(255, 0, 0));
                jLabelError.setText("Fehler bei Ausf�hrung des Befehls!");
        }
}//GEN-LAST:event_jButtonSaveAndExitActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TopicSubGUI(status, topicID, name, description).setVisible(true);
            }
        });
    }

    public void newTopicInit() {
        jLabelError.setForeground(new java.awt.Color(255, 0, 0));
        jLabelError.setText("Es m�ssen alle Felder ausgew�hlt werden");
    }

    public void editTopicInit() {
        jTextFieldTopicName.setText(name);
        jTextFieldTopicDescription.setText(description);
    }

    public void newTopic() {
        name = jTextFieldTopicName.getText();
        description = jTextFieldTopicDescription.getText();
        if(name != "") {
            DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
            dbCon.openDB();
            con = dbCon.getCon();
            try {
                Statement stmt = con.createStatement();
                String sql = "INSERT INTO Thema (Name, Beschreibung) VALUES " +
                        "('" + name + "', '" + description + "')";
                stmt.executeUpdate(sql);
                stmt.close();
            } catch(Exception e) {
                System.out.println(e.toString());
                System.exit(1);
            }
            dbCon.closeDB(con);
        }
        else {
            jLabelError.setText("Das Feld Thema muss ausgef�llt werden!");
        }
    }

    public void editTopic() {
        name = jTextFieldTopicName.getText();
        description = jTextFieldTopicDescription.getText();
        if(name != "") {
            DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
            dbCon.openDB();
            con = dbCon.getCon();

            try {
                Statement stmt = con.createStatement();
                String sql = "UPDATE Thema SET Name='" + name
                            + "' WHERE ThemaID=" + topicID;
                stmt.executeUpdate(sql);
                String sql2 = "UPDATE Thema SET Beschreibung='" + description
                            + "' WHERE ThemaID=" + topicID;
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
            jLabelError.setText("Das Feld Thema muss ausgef�llt werden!");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSaveAndExit;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelError;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldTopicDescription;
    private javax.swing.JTextField jTextFieldTopicName;
    // End of variables declaration//GEN-END:variables

}
