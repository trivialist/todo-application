/*
 * MeetingTypeGUI.java
 *
 * Created on 27. Dezember 2006, 23:58
 */

package todo.gui;

import todo.subgui.MeetingTypeSubGUI;
import todo.dialog.DeleteMeetingTypeDialog;
import todo.tablemodel.MeetingTypeTableModel;

/**
 *
 * @author  Marcus Hertel
 */
public class MeetingTypeGUI extends javax.swing.JFrame {
    
    private static MeetingTypeGUI mtGUI = new MeetingTypeGUI();
    /** Creates new form MeetingTypeGUI */
    public MeetingTypeGUI() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonNewMeetingType = new javax.swing.JButton();
        jButtonEditMeetingType = new javax.swing.JButton();
        jButtonDeleteMeetingType = new javax.swing.JButton();
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

        jButtonNewMeetingType.setText("Neu...");
        jButtonNewMeetingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewMeetingTypeActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonNewMeetingType, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButtonEditMeetingType.setText("Bearbeiten...");
        jButtonEditMeetingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditMeetingTypeActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonEditMeetingType, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        jButtonDeleteMeetingType.setText("L�schen");
        jButtonDeleteMeetingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteMeetingTypeActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDeleteMeetingType, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 40));

        jTable1.setModel(new MeetingTypeTableModel());
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 470, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewMeetingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewMeetingTypeActionPerformed
        String meetingType = "";
        int meetingTypeID = 0;
        MeetingTypeSubGUI newMt = new MeetingTypeSubGUI(0, meetingTypeID, meetingType);
        newMt.setVisible(true);
    }//GEN-LAST:event_jButtonNewMeetingTypeActionPerformed

    private void jButtonEditMeetingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditMeetingTypeActionPerformed
         if(jTable1.getSelectedRow() != -1) {
            Object meetingType = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            Object meetingTypeID = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            Integer temp = new Integer(String.valueOf(meetingTypeID));
            int mtID = temp.intValue();
            if(meetingType != null) {
                MeetingTypeSubGUI newMt = new MeetingTypeSubGUI(1, mtID, String.valueOf(meetingType));
                newMt.setVisible(true);
            }
         }
    }//GEN-LAST:event_jButtonEditMeetingTypeActionPerformed

    private void jButtonDeleteMeetingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteMeetingTypeActionPerformed
        if(jTable1.getSelectedRow() != -1) {
            Object meetingType = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            Object meetingTypeID = jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            Integer temp = new Integer(String.valueOf(meetingTypeID));
            int mtID = temp.intValue();
            if(meetingType != null) {
                DeleteMeetingTypeDialog delMT = new DeleteMeetingTypeDialog(mtGUI, true, mtID, String.valueOf(meetingType));
            delMT.setVisible(true);
            }
        }        
    }//GEN-LAST:event_jButtonDeleteMeetingTypeActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if(evt.WINDOW_GAINED_FOCUS != 1) {
            jTable1.setModel(new MeetingTypeTableModel());
        }
    }//GEN-LAST:event_formWindowGainedFocus
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteMeetingType;
    private javax.swing.JButton jButtonEditMeetingType;
    private javax.swing.JButton jButtonNewMeetingType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
