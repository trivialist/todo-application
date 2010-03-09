/*
 * NoPersonalTodoDialog.java
 *
 * Created on 8. November 2007, 13:47
 */

package todo.dialog;

/**
 *
 * @author  Administrator
 */
public class NoPersonalTodoDialog extends javax.swing.JDialog {
    
    private static String employee;
    /** Creates new form NoPersonalTodoDialog */
    public NoPersonalTodoDialog(java.awt.Frame parent, boolean modal, String emp) {
        super(parent, modal);
        initComponents();
        this.employee = emp;
        jLabelEmployee.setText(employee);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelEmployee = new javax.swing.JLabel();
        jButtonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Keine pers�nlichen Daten f�r ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jLabel2.setText("vorhanden");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, -1));

        jLabelEmployee.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabelEmployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 180, 20));

        jButtonOk.setText("OK");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NoPersonalTodoDialog(new javax.swing.JFrame(), true, employee).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelEmployee;
    // End of variables declaration//GEN-END:variables
    
}