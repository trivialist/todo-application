/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SearchResultSubGUI.java
 *
 * Created on 27.04.2011, 13:30:40
 */
package todo.subgui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import todo.core.Area;
import todo.core.Category;
import todo.core.FinStatus;
import todo.core.Institution;
import todo.core.Meeting;
import todo.core.MeetingType;
import todo.core.Memo;
import todo.core.Todo;
import todo.core.Topic;
import todo.dialog.TodoNoteDialog;
import todo.tablemodel.SearchResultTableModel;

/**
 *
 * @author Sven
 */
public class SearchResultSubGUI extends javax.swing.JFrame
{
	SearchResultTableModel searchResult;

	/** Creates new form SearchResultSubGUI */
	public SearchResultSubGUI(SearchResultTableModel searchResult)
	{
		this.searchResult = searchResult;
		initComponents();

		setExtendedState(MAXIMIZED_BOTH);

		for (int i = 0; i < searchResult.getRowCount(); i++)
		{
			jTable1.setRowHeight(i, searchResult.getRowHeight(i));
		}

		jTable1.getRowSorter().toggleSortOrder(0);
		jTable1.getRowSorter().toggleSortOrder(0);

		jTable1.setDefaultRenderer(Object.class, new MyCellRenderer());
		jTable1.getColumnModel().getColumn(0).setPreferredWidth(70);
		jTable1.getColumnModel().getColumn(1).setPreferredWidth(900);
		jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);

		jTable1.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					openElement(jTable1.getSelectedRow());
				}
			}
		});
	}

	private void openElement(int rowIndex)
	{
		Class classType = (Class) jTable1.getValueAt(rowIndex, -2);

		if (classType == Todo.class)
		{
			new TodoSubGUI(1, 0, "", "", (Integer) jTable1.getValueAt(rowIndex, -1), true).setVisible(true);
		}
		else if (classType == Meeting.class)
		{
			new MeetingSubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null, null, null).setVisible(true);
		}
		else if (classType == Area.class)
		{
			new AreaSubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null, null).setVisible(true);
		}
		else if (classType == Institution.class)
		{
			new InstitutionSubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null).setVisible(true);
		}
		else if (classType == Category.class)
		{
			new CategorySubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null, null).setVisible(true);
		}
		else if (classType == Memo.class)
		{
			TodoNoteDialog.showMemoPopup((Integer) jTable1.getValueAt(rowIndex, -1));
		}
		else if (classType == MeetingType.class)
		{
			new MeetingTypeSubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null).setVisible(true);
		}
		else if (classType == FinStatus.class)
		{
			new FinStatusSubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null).setVisible(true);
		}
		else if (classType == Topic.class)
		{
			new TopicSubGUI(1, (Integer) jTable1.getValueAt(rowIndex, -1), null, null).setVisible(true);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Suchergebnis");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(searchResult);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
