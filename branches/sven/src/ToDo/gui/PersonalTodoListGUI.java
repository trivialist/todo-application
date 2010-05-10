/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PersonalTodoListGUI.java
 *
 * Created on 07.10.2009, 11:02:42
 */
package todo.gui;

import todo.gui.GlobalError;
import todo.subgui.TodoSubGUI;
import todo.dbcon.DB_ToDo_Connect;
import todo.tablemodel.PersonalTodoTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Hertel
 */
public class PersonalTodoListGUI extends javax.swing.JFrame
{

	private int empID;
	private int statID;
	private static Connection con;

	/** Creates new form PersonalTodoListGUI */
	public PersonalTodoListGUI(int empID, int statID)
	{
		this.empID = empID;
		this.statID = statID;
		initComponents();
		jTable1.setAutoCreateRowSorter(true);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonDetails = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OP Liste");
        setMinimumSize(new java.awt.Dimension(1000, 600));
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jScrollPane1.setMinimumSize(new java.awt.Dimension(1000, 600));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButtonDetails.setText("Details");
        jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonDetails)
                .addContainerGap(923, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonDetails)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
		if (evt.WINDOW_GAINED_FOCUS != 1)
		{
			jTable1.setModel(new PersonalTodoTableModel(empID, statID));
			jTable1.setAutoCreateRowSorter(true);
		}
    }//GEN-LAST:event_formWindowGainedFocus

	/*
	 * Bei Doppelklick auf Tabellenzeile wird der entsprechende ProtokollPunkt
	 * mit Status=0(bearbeiten) ge�ffnet
	 *
	 */
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
		if (evt.getClickCount() == 2)
		{
			if (jTable1.getSelectedRow() != -1)
			{
				Object cat = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
				Object top = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
				Object tdID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
				Integer temp = new Integer(String.valueOf(tdID));
				int tID = temp.intValue();
				if (cat != null)
				{
					TodoSubGUI newTodo = new TodoSubGUI(1, getMeetingIdByTodoID(tID),
							String.valueOf(cat), String.valueOf(top), tID, true);
					newTodo.setVisible(true);
				}
			}
		}
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			Object cat = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
			Object top = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
			Object tdID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
			Integer temp = new Integer(String.valueOf(tdID));
			int tID = temp.intValue();
			if (cat != null)
			{
				TodoSubGUI newTodo = new TodoSubGUI(1, getMeetingIdByTodoID(tID),
						String.valueOf(cat), String.valueOf(top), tID, true);
				newTodo.setVisible(true);
			}
		}
    }//GEN-LAST:event_jButtonDetailsActionPerformed

	public int getMeetingIdByTodoID(int todoID)
	{
		int mID = 0;
		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();

		try
		{
			Statement stmt = con.createStatement();
			String sql = "SELECT ToDoID, SitzungsID FROM Protokollelement WHERE ToDoID=" + todoID;
			ResultSet rst = stmt.executeQuery(sql);

			while (rst.next())
			{
				mID = rst.getInt("SitzungsID");
			}
			rst.close();
			stmt.close();

		} catch (Exception ex)
		{
			Logger.getLogger(PersonalTodoListGUI.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
		DB_ToDo_Connect.closeDB(con);
		return mID;
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDetails;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
