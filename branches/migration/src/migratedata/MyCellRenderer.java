/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedata;

import java.awt.Color;
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

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		setVerticalAlignment(SwingConstants.TOP);

		if (column == 1)
		{
			return newComponent(table, value, isSelected, hasFocus, row, column);
		}

		return this;
	}

	private Component newComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JTextArea x = new JTextArea(value.toString());
		x.setFont(Font.getFont("Arial"));
		if (isSelected)
		{
			x.setBackground(UIManager.getColor("Table.selectionBackground"));
			x.setForeground(UIManager.getColor("Table.selectionForeground"));
		}
		return x;
	}
}
