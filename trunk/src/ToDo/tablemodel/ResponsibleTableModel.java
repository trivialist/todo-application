/*
 * ResponsibleTableModel.java
 *
 * Created on 22. Januar 2007, 05:21
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
import java.util.Enumeration;

/**
 *
 * @author Marcus Hertel
 */
public class ResponsibleTableModel extends AbstractTableModel {
    
    /* Sitzungsarten-Objekte welche zeilenweise agezeigt werden sollen */
    protected ArrayList<Employee> employeeObjects = new ArrayList<Employee>();
    private String[] columnNames = new String[3];
    private Vector colNam = new Vector();   //Zwischenspeicher f�r Array columnNames
    private static Connection con; 
    private Vector responsible = new Vector();
    private int meetingID;
    
    /** Creates a new instance of ResponsibleTableModel */
    public ResponsibleTableModel(Vector responsible, int meetingID) {
        this.responsible = responsible;
        this.meetingID = meetingID;
        this.loadData();
    }
    
   public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.employeeObjects.get(zeile).getEmployeeID();
        case 1 :
            return this.employeeObjects.get(zeile).getLastName();
        case 2 :
            return this.employeeObjects.get(zeile).getName();
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
        loadPartFromDB();
        DB_Mitarbeiter_Connect dbCon = new DB_Mitarbeiter_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        
        Enumeration partEnum = responsible.elements ();
        while (partEnum.hasMoreElements ()) {
            int employeeID= Integer.valueOf(partEnum.nextElement().toString()).intValue();
       
            try {
                Statement stmt = con.createStatement();
                String sql = "SELECT * FROM Stammdaten WHERE Personalnummer = " 
                            + employeeID;
                ResultSet rst = stmt.executeQuery(sql);

                while(rst.next()) {
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
        }
        dbCon.closeDB(con);
    }
    
    public void loadPartFromDB() {
        /*
         * es fehlt: wenn schon Teilnehmer eingegeben wurden, welche also in
         * der DB als String aus ID's stehen, m�ssen diese noch mittels StringTokenizer
         * zerlegt werden und mittels DB in Namen gewandelt werden.
         *
         */     
    }
    
    public void setColumnNames() {
        colNam.add("Nummer");
        colNam.add("Nachname");
        colNam.add("Vorname");
        colNam.toArray(columnNames);
    }
    
}