/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HeadingMigrate.java
 *
 * Created on 19.04.2010, 13:14:52
 */
package migratedata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author sven
 */
public class HeadingMigrate extends javax.swing.JFrame
{

	private Connection con;
	private int meetingId;
	private ResultSet rst;
	private Statement stmt;

	/** Creates new form HeadingMigrate */
	public HeadingMigrate()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception ex)
		{
			Logger.getLogger(HeadingMigrate.class.getName()).log(Level.SEVERE, null, ex);
		}

		DB_ToDo_Connect.openDB();
		con = DB_ToDo_Connect.getCon();
		initComponents();

		DB_ToDo_Connect.getCon();
		con = DB_ToDo_Connect.getCon();

		try
		{
			stmt = con.createStatement();
			rst = stmt.executeQuery("SELECT * FROM Sitzungsdaten INNER JOIN Sitzungsart ON Sitzungsdaten.SitzungsartID = Sitzungsart.SitzungsartID WHERE Tagesordnung <> ''");

			if (rst.next())
			{
				meetingId = rst.getInt("SitzungsdatenID");
				jTable1.setModel(new MigrateTableModel(meetingId));
				jTextField1.setText(rst.getString("Name"));
				jTextField2.setText(rst.getString("Datum"));
				jTextField3.setText(rst.getString("Ort"));
				jTextArea1.setText(rst.getString("Tagesordnung"));
			}

		} catch (SQLException ex)
		{
			Logger.getLogger(HeadingMigrate.class.getName()).log(Level.SEVERE, null, ex);
		}

		jTable1.setDefaultRenderer(Object.class, new MyCellRenderer());

		this.setExtendedState(MAXIMIZED_BOTH);

		jTable1.getColumnModel().getColumn(0).setPreferredWidth(160);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(500);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(550);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Daten migrieren");

        jLabel1.setText("Alte Tagesordnung:");

        jLabel2.setText("Sitzung:");

        jTextField1.setEditable(false);
        jTextField1.setText("sd");

        jLabel3.setText("Datum:");

        jTextField2.setEditable(false);

        jTextField3.setEditable(false);

        jLabel4.setText("Ort:");

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setText("sds");
        jScrollPane1.setViewportView(jTextArea1);

        jLabel5.setText("Zugeordnete Protokollelemente:");

        jButton1.setText("Speichern & weiter");

        jTable1.setModel(new MigrateTableModel(-1));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setRowHeight(112);
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{

			public void run()
			{
				new HeadingMigrate().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}