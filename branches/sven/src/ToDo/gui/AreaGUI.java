/*
 * AreaGUI.java
 *
 * Created on 23. Juli 2007, 14:19
 */

package todo.gui;

import todo.subgui.AreaSubGUI;
import todo.dialog.DeleteAreaDialog;
import todo.tablemodel.AreaTableModel;


/**
 *
 * @author  Marcus Hertel
 */
public class AreaGUI extends javax.swing.JFrame {
    
    private static AreaGUI areaGUI = new AreaGUI();
    /** Creates new form AreaGUI */
    public AreaGUI() {
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
        jButtonNewArea = new javax.swing.JButton();
        jButtonEditArea = new javax.swing.JButton();
        jButtonDeleteArea = new javax.swing.JButton();
        jLabelArea = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bereiche verwalten");
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonNewArea.setText("Neu...");
        jButtonNewArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewAreaActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonNewArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jButtonEditArea.setText("Bearbeiten...");
        jButtonEditArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditAreaActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonEditArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        jButtonDeleteArea.setText("L�schen");
        jButtonDeleteArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAreaActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDeleteArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabelArea.setBorder(javax.swing.BorderFactory.createTitledBorder("Bereich"));
        jPanel1.add(jLabelArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 60));

        jTable1.setModel(new AreaTableModel());
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 470, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if(evt.WINDOW_GAINED_FOCUS != 1) {
            jTable1.setModel(new AreaTableModel());
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void jButtonDeleteAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAreaActionPerformed
        if(jTable1.getSelectedRow() != -1) {
            Object areaID = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            Object areaName = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            Object areaDescription = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            Integer temp = new Integer(String.valueOf(areaID));
            int aID = temp.intValue();
            if(areaName != null) {
                DeleteAreaDialog delArea = new DeleteAreaDialog(areaGUI, true,
                        aID, String.valueOf(areaName),
                        String.valueOf(areaDescription));
                delArea.setVisible(true);
            }
        }
    }//GEN-LAST:event_jButtonDeleteAreaActionPerformed

    private void jButtonEditAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditAreaActionPerformed
        if(jTable1.getSelectedRow() != -1) {
            Object areaID = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            Object areaName = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            Object areaDescription = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            Integer temp = new Integer(String.valueOf(areaID));
            int aID = temp.intValue();
            if(areaName != null) {
                AreaSubGUI newArea = new AreaSubGUI(1, aID,
                        String.valueOf(areaName), String.valueOf(areaDescription));
                newArea.setVisible(true);
            }
        }
    }//GEN-LAST:event_jButtonEditAreaActionPerformed

    private void jButtonNewAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewAreaActionPerformed
        int areaID = 0;
        String areaName = "";
        String areaDescription = "";
        AreaSubGUI newArea = new AreaSubGUI(0, areaID, areaName, areaDescription);
        newArea.setVisible(true);
    }//GEN-LAST:event_jButtonNewAreaActionPerformed
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AreaGUI().setVisible(true);
            }
        });
    }*/
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteArea;
    private javax.swing.JButton jButtonEditArea;
    private javax.swing.JButton jButtonNewArea;
    private javax.swing.JLabel jLabelArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
