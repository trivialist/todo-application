/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DeleteTopicDialog.java
 *
 * Created on 29.05.2009, 15:09:31
 */
package todo.dialog;

import todo.gui.GlobalError;
import todo.dbcon.DB_ToDo_Connect;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hertel
 */
public class DeleteTopicDialog extends javax.swing.JDialog
{

	private int topicID = 0;
	private String name = "";
	private String description = "";
	private static Connection con;

	/** Creates new form DeleteTopicDialog */
	public DeleteTopicDialog(java.awt.Frame parent, boolean modal,
			int topicID, String name, String description)
	{
		super(parent, modal);
		this.topicID = topicID;
		this.name = name;
		this.description = description;
		initComponents();
		setTitle("Thema " + name + " l�schen?");
		jLabelArea.setText(name);
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
        jButtonCancel = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabelArea = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

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

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("    wirklich l�schen?");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 140, 20));

        jLabelArea.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabelArea.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelArea.setText("Fehler");
        jPanel1.add(jLabelArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 160, 20));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("M�chten Sie dieses Thema");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 260, 140));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
		setVisible(false);
}//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
		deleteTopic(topicID);
		setVisible(false);
}//GEN-LAST:event_jButtonOkActionPerformed

	public void deleteTopic(int topicID)
	{
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();
		try
		{
			Statement stmt = con.createStatement();
			String sql = "DELETE FROM Thema WHERE ThemaID=" + topicID;
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception ex)
		{
			Logger.getLogger(DeleteTopicDialog.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelArea;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}