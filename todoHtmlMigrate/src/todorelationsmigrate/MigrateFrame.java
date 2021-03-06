/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MigrateFrame.java
 *
 * Created on 06.04.2011, 08:52:08
 */
package todorelationsmigrate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sven
 */
public class MigrateFrame extends javax.swing.JFrame
{
	private Connection todoConn, personnelConn;

	/** Creates new form MigrateFrame */
	public MigrateFrame()
	{
		initComponents();

		DB_ToDo_Connect.openDB();
		DB_Mitarbeiter_Connect.openDB();

		todoConn = DB_ToDo_Connect.getCon();
		personnelConn = DB_Mitarbeiter_Connect.getCon();
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
        dropped = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dropped.setColumns(20);
        dropped.setRows(5);
        jScrollPane1.setViewportView(dropped);

        jLabel1.setText("Dropped personnelID's:");

        jButton1.setText("Go for it!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private boolean itemExists(String destinationTable, String destinationColumn, Integer destinationId, Integer personnelId)
	{
		try
		{
			Statement statement = todoConn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT " + destinationColumn + " FROM " + destinationTable + " WHERE " + destinationColumn + " = " + destinationId + " AND personnelID = " + personnelId);

			resultSet.next();
			resultSet.getInt("destinationColumn");

			dropped.setText(dropped.getText() + "Item exists already\n");

			return true;
		}
		catch (Exception ex)
		{
			//do nothing
			return false;
		}
	}

	private ArrayList<Integer> getValidPersonnelIds(String oldList)
	{
		ArrayList<Integer> validList = new ArrayList<Integer>();

		StringTokenizer tokenizer = new StringTokenizer(oldList, ", ");
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			try
			{
				Statement statement = personnelConn.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT Personalnummer FROM stammdaten WHERE Personalnummer = " + token);

				resultSet.next();

				validList.add(resultSet.getInt("Personalnummer"));
			}
			catch (Exception ex)
			{
				//log that for user
				dropped.setText(dropped.getText() + "Dropped " + token + "\n");
			}
		}

		return validList;
	}

	private void insertRelation(String destinationTable, String destinationColumn, Integer personnelId, Integer destinationId)
	{
		try
		{
			Statement statement = todoConn.createStatement();
			statement.execute("INSERT INTO " + destinationTable + " (personnelID, " + destinationColumn + ") VALUES (" + personnelId + ", " + destinationId + ")");
		}
		catch (Exception ex)
		{
			//do nothing
			dropped.setText(dropped.getText() + "Insert error\n");
		}
	}

	private void migrateFields(String sourceTable, String sourceColumn, String destinationTable, String destinationColumn, String sourceIdColumn)
	{
		try
		{
			Statement statement = todoConn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + sourceTable);

			while (resultSet.next())
			{
				String resolveColumn = resultSet.getString(sourceColumn);
				Integer destinationId = resultSet.getInt(sourceIdColumn);

				if (resolveColumn == null || (resolveColumn != null && resolveColumn.equals("")))
				{
					continue;
				}

				ArrayList<Integer> validIds = getValidPersonnelIds(resolveColumn);

				for (Integer currentPersonnelId : validIds)
				{
					if (!itemExists(destinationTable, destinationColumn, destinationId, currentPersonnelId))
					{
						insertRelation(destinationTable, destinationColumn, currentPersonnelId, destinationId);
						dropped.setText(dropped.getText() + "Inserted...\n");
					}
				}
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(MigrateFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		new Thread(new Runnable()
		{
			public void run()
			{
				migrateFields("Protokollelement", "Verantwortliche", "todo_responsible_personnel", "todoID", "ToDoID");
				migrateFields("Protokollelement", "Beteiligte", "todo_involved_personnel", "todoID", "ToDoID");
				migrateFields("Sitzungsdaten", "Teilnehmer", "meeting_attendee_personnel", "meetingID", "SitzungsdatenID");
			}
		}).start();
	}//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea dropped;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
