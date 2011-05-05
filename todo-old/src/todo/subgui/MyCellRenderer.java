/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.subgui;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author sven
 */
public class MyCellRenderer extends DefaultTableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setVerticalAlignment(SwingConstants.TOP);

		if (column == 1)
		{
			return newComponent(table, value, isSelected, hasFocus, row, column);
		}

		return this;
	}

	private Component newComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JTextArea textAreaComponent = new JTextArea(value.toString());
		textAreaComponent.setFont(Font.getFont("Arial"));
		textAreaComponent.setRows(3);

		if (isSelected)
		{
			textAreaComponent.setBackground(UIManager.getColor("Table.selectionBackground"));
			textAreaComponent.setForeground(UIManager.getColor("Table.selectionForeground"));
		}
		
		return textAreaComponent;
	}
}
