/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MigrateFrame.java
 *
 * Created on 06.04.2011, 08:52:08
 */
package todohtmlmigrate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import todo.gui.htmleditor.HTMLEditor;

/**
 *
 * @author Sven
 */
public class MigrateFrame extends javax.swing.JFrame
{
	private Connection todoConn;
	Semaphore sem = new Semaphore(0);
	HTMLEditor htmledit = new HTMLEditor();

	/** Creates new form MigrateFrame */
	public MigrateFrame()
	{
		initComponents();

		DB_ToDo_Connect.openDB();
		DB_Mitarbeiter_Connect.openDB();

		todoConn = DB_ToDo_Connect.getCon();

		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		jScrollPane1.setViewportView(htmledit);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HTML Migrate");

        jButton1.setText("Go for it!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Next!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTextPane2);

        jButton3.setText("Show!");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(5, 5, 5)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void initProgressBar() throws Exception
	{
		Statement stmtcnt = todoConn.createStatement();
		ResultSet rstcnt = stmtcnt.executeQuery("SELECT COUNT(ToDoID) AS count FROM Protokollelement");
		rstcnt.next();
		jProgressBar1.setMinimum(0);
		jProgressBar1.setMaximum(rstcnt.getInt("count"));
		rstcnt.close();
		stmtcnt.close();
	}

	private ArrayList<String> getTrimmedLines(String string)
	{
		String lines[] = string.split("(\n)");
		ArrayList<String> returnValue = new ArrayList<String>();

		for(int i=0; i<lines.length; i++)
		{
			returnValue.add(lines[i].trim().trim().trim());
		}

		return returnValue;
	}

	private String mergeLines(ArrayList<String> lines)
	{
		String returnString = "";
		
		for(int i=0; i<lines.size(); i++)
		{
			if(i < lines.size())
			{
				returnString = returnString + lines.get(i) + "\n";
			}
			else
			{
				returnString = returnString + lines.get(i);
			}
		}

		return returnString;
	}

	private boolean isUnorderedElement(String line)
	{
		return line.matches("^(\\-\\->|\\-|=>|\\*).+");
	}

	private String convertUnorderedElement(String line)
	{
		Pattern p = Pattern.compile("^(\\-\\->|\\-|=>|\\*)\\s*(.*)");
		Matcher m = p.matcher(line);
		m.find();

		return "<li>" + m.group(2) + "</li>";
	}

	private boolean isOrderedElement(String line)
	{
		return line.matches("^(\\d{1,2}\\.( |\\t)).+");
	}

	private String convertOrderedElement(String line)
	{
		Pattern p = Pattern.compile("^(\\d{1,2}\\.( |\\t))(.+)");
		Matcher m = p.matcher(line);
		m.find();

		return "<li>" + m.group(3) + "</li>";
	}

	private boolean tagFollows(ArrayList<String> lines, int currentIndex)
	{
		if(currentIndex + 1 >= lines.size())return false;

		return (lines.get(currentIndex + 1).startsWith("<ul>")) ? true : false;
	}

	private boolean emptyLineFollows(ArrayList<String> lines, int currentIndex)
	{
		if(currentIndex + 1 >= lines.size())return false;

		return (lines.get(currentIndex + 1).isEmpty()) ? true : false;
	}

	private ArrayList<String> migrateParagraphs(ArrayList<String> lines)
	{
		ArrayList<String> result = new ArrayList<String>();

		boolean isParagraphOpen = false;

		for(int i=0; i<lines.size(); i++)
		{
			String line = lines.get(i);

			if(line.startsWith("<li>"))
			{
				result.add(line);
			}
			else if(line.startsWith("<ul>") || line.startsWith("<ol>"))
			{
				if(isParagraphOpen == true)
				{
					isParagraphOpen = false;
					result.add("</p>");
				}

				result.add(line);
			}
			else if(line.startsWith("</ul>") || line.startsWith("</ol>"))
			{
				result.add(line);
			}
			else if(line.isEmpty())
			{
				if(isParagraphOpen == true)
				{
					isParagraphOpen = false;
					result.add("</p>");
				}
			}
			else
			{
				if(isParagraphOpen == false)
				{
					isParagraphOpen = true;
					result.add("<p>");
				}

				if(tagFollows(lines, i) || emptyLineFollows(lines, i) || i + 1 == lines.size())
				{
					result.add(line);
				}
				else
				{
					result.add(line + "<br/>");
				}
			}
		}

		if(isParagraphOpen == true)
		{
			result.add("</p>");
		}

		return result;
	}

	private ArrayList<String> migrateLists(ArrayList<String> lines)
	{
		ArrayList<String> result = new ArrayList<String>();

		boolean isUnorderedOpen = false;
		boolean isOrderedOpen = false;

		for(String line : lines)
		{
			if(isUnorderedElement(line))
			{
				if(isOrderedOpen == true)
				{
					isOrderedOpen = false;
					result.add("</ol>");
				}

				if(isUnorderedOpen == false)
				{
					isUnorderedOpen = true;
					result.add("<ul>");
				}

				result.add(convertUnorderedElement(line));
			}
			else if(isOrderedElement(line))
			{
				if(isUnorderedOpen == true)
				{
					isUnorderedOpen = false;
					result.add("</ul>");
				}

				if(isOrderedOpen == false)
				{
					isOrderedOpen = true;
					result.add("<ol>");
				}

				result.add(convertOrderedElement(line));
			}
			else
			{
				if(isUnorderedOpen == true)
				{
					isUnorderedOpen = false;
					result.add("</ul>");
				}

				if(isOrderedOpen == true)
				{
					isOrderedOpen = false;
					result.add("</ol>");
				}

				result.add(line);
			}
		}

		if(isUnorderedOpen == true)
		{
			isUnorderedOpen = false;
			result.add("</ul>");
		}

		if(isOrderedOpen == true)
		{
			isOrderedOpen = false;
			result.add("</ol>");
		}

		return result;
	}

	private void migrateOneElement(String content, int id) throws Exception
	{
		if(content == null || content.equals(""))return;

		jTextPane2.setText(content);
		String html = mergeLines(migrateParagraphs(migrateLists(getTrimmedLines(content))));
		htmledit.setText(html);
		//System.out.println(html);
		//System.out.println("-----------------------------------------------");

		//sem.acquire();
		
                System.out.println(id);
                
		PreparedStatement pstmt = todoConn.prepareStatement("UPDATE Protokollelement SET Inhalt = ? WHERE ToDoID = ?");
		pstmt.setString(1, html);
		pstmt.setInt(2, id);
                pstmt.executeUpdate();
		pstmt.close();
	}

	private void migrateAllElements() throws Exception
	{
		initProgressBar();

		Statement stmt = todoConn.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT Inhalt, ToDoID FROM Protokollelement ORDER BY ToDoID DESC");

		while(rst.next())
		{
			migrateOneElement(rst.getString("Inhalt"), rst.getInt("ToDoID"));
			jProgressBar1.setValue(jProgressBar1.getValue() + 1);
		}

		rst.close();
		stmt.close();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					migrateAllElements();
				}
				catch (Exception ex)
				{
					Logger.getLogger(MigrateFrame.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}).start();
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
		//sem.release();
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
	{//GEN-HEADEREND:event_jButton3ActionPerformed
		System.out.println("###################");
		System.out.println(htmledit.getText());
		System.out.println("###################");
	}//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables
}
