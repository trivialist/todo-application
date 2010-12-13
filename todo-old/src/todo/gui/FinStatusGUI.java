/*
 * FinStatusGUI.java
 *
 * Created on 29. Dezember 2006, 17:03
 */
package todo.gui;

import todo.subgui.FinStatusSubGUI;
import todo.dialog.DeleteFinStatusDialog;
import todo.tablemodel.FinStatusTableModel;

/**
 *
 * @author  Marcus Hertel
 */
public class FinStatusGUI extends javax.swing.JFrame
{

	private static FinStatusGUI fsGUI = new FinStatusGUI();

	/** Creates new form FinStatusGUI */
	public FinStatusGUI()
	{
		initComponents();
		jTable1.setAutoCreateRowSorter(true);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonNewFinStatus = new javax.swing.JButton();
        jButtonEditFinStatus = new javax.swing.JButton();
        jButtonDeleteFinStatus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonNewFinStatus.setText("Neu...");
        jButtonNewFinStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewFinStatusActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonNewFinStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButtonEditFinStatus.setText("Bearbeiten...");
        jButtonEditFinStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditFinStatusActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonEditFinStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        jButtonDeleteFinStatus.setText("L�schen");
        jButtonDeleteFinStatus.setEnabled(false);
        jButtonDeleteFinStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteFinStatusActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDeleteFinStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 40));

        jTable1.setModel(new FinStatusTableModel());
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 470, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
		if (evt.WINDOW_GAINED_FOCUS != 1)
		{
			jTable1.setModel(new FinStatusTableModel());
		}
    }//GEN-LAST:event_formWindowGainedFocus

    private void jButtonDeleteFinStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteFinStatusActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			Object statusName = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
			Object statusID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
			Integer temp = new Integer(String.valueOf(statusID));
			int fsID = temp.intValue();
			if (statusName != null)
			{
				DeleteFinStatusDialog delFS = new DeleteFinStatusDialog(fsGUI, true, fsID, String.valueOf(statusName));
				delFS.setVisible(true);
			}
		}
    }//GEN-LAST:event_jButtonDeleteFinStatusActionPerformed

    private void jButtonEditFinStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditFinStatusActionPerformed
		if (jTable1.getSelectedRow() != -1)
		{
			Object statusName = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
			Object statusID = jTable1.getValueAt(jTable1.getSelectedRow(), -1);
			Integer temp = new Integer(String.valueOf(statusID));
			int fsID = temp.intValue();
			if (statusName != null)
			{
				FinStatusSubGUI newFs = new FinStatusSubGUI(1, fsID, String.valueOf(statusName));
				newFs.setVisible(true);
			}
		}
    }//GEN-LAST:event_jButtonEditFinStatusActionPerformed

    private void jButtonNewFinStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewFinStatusActionPerformed
		String statusName = "";
		int statusID = 0;
		FinStatusSubGUI newFs = new FinStatusSubGUI(0, statusID, statusName);
		newFs.setVisible(true);
    }//GEN-LAST:event_jButtonNewFinStatusActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteFinStatus;
    private javax.swing.JButton jButtonEditFinStatus;
    private javax.swing.JButton jButtonNewFinStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
