/*
 * EmployeeTableModel.java
 *
 * Created on 22. Januar 2007, 01:19
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.tablemodel;

import todo.core.Employee;
import todo.dbcon.DB_Mitarbeiter_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
/**
 *
 * @author Marcus Hertel
 */
public class EmployeeTableModel extends AbstractTableModel{
    
    /* Mitarbeiter-Objekte welche zeilenweise angezeigt werden sollen */
    protected ArrayList<Employee> employeeObjects = new ArrayList<Employee>();
    private String[] columnNames = new String[2];
    private Vector colNam = new Vector();   //Zwischenspeicher für Array columnNames
    private static Connection con; 
    
    /**
     * Creates a new instance of EmployeeTableModel 
     */
    public EmployeeTableModel() {
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.employeeObjects.get(zeile).getLastName();
        case 1 :
            return this.employeeObjects.get(zeile).getName();
        case -1 :
            return this.employeeObjects.get(zeile).getEmployeeID();
        default:
          return null;
        }
    }
    
    /*
     * return Anzahl der Sitzungsarten-Objekte
     */
    public int getRowCount() {
        return this.employeeObjects.size();
    }
    
    public int getColumnCount() {
        return  this.columnNames.length;
    }
    
    public String getColumnName(final int spalte) {
        setColumnNames();
        if(spalte < this.getColumnCount()) {
            return columnNames[spalte];
        }
        else {
            return super.getColumnName(spalte);
        }
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    protected void loadData() {
        DB_Mitarbeiter_Connect dbCon = new DB_Mitarbeiter_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Stammdaten ORDER BY Nachname ASC";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                int employeeID = rst.getInt("Personalnummer");
                String lastName = rst.getString("Nachname");    
                String name = rst.getString("Vorname");
                employeeObjects.add(new Employee(employeeID, name, lastName));
            }
            rst.close();
            stmt.close();
        }
        catch(Exception e) {
            System.out.println(e.toString()); 
            System.exit(1); 
        }
        dbCon.closeDB(con);
    }
    
    public void setColumnNames() {
        colNam.add("Nachname");
        colNam.add("Vorname");
        colNam.toArray(columnNames);
    }
}
