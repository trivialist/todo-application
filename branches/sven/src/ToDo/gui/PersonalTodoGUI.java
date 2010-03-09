/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.gui;


import todo.tablemodel.PersonalTodoTableModel;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author Marcus Hertel
 */
public final class PersonalTodoGUI extends JFrame {
    private JTable __table;
    private JScrollPane __scrollPane;
    private static int emp;
    private static int stat;

    public PersonalTodoGUI(int emp, int stat) {
        super("Todo-Liste");
        this.emp = emp;
        this.stat = stat;
        TableCellRenderer defaultRenderer;

        __table = new JTable(new PersonalTodoTableModel(emp, stat));
        
        defaultRenderer = __table.getDefaultRenderer(JButton.class);
        __table.setDefaultRenderer(JButton.class,
			       new todo.gui.JTableButtonRenderer(defaultRenderer));
        __table.setPreferredScrollableViewportSize(new Dimension(700, 400));
        __table.setFillsViewportHeight(true);

        __table.addMouseListener(new JTableButtonMouseListener(__table));
        __scrollPane = new JScrollPane(__table);
        setContentPane(__scrollPane);
        
    }

}
