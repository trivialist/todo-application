/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;


/**
 *
 * @author hertel
 */

class JTableButtonRenderer implements TableCellRenderer {
  private TableCellRenderer __defaultRenderer;

  public JTableButtonRenderer(TableCellRenderer renderer) {
    __defaultRenderer = renderer;
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
						 boolean isSelected,
						 boolean hasFocus,
						 int row, int column)
  {
    if(value instanceof Component)
      return (Component)value;
    return __defaultRenderer.getTableCellRendererComponent(
	   table, value, isSelected, hasFocus, row, column);
  }
}


