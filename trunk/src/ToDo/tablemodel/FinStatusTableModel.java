/*
 * FinStatusTableModel.java
 *
 * Created on 29. Dezember 2006, 16:57
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.tablemodel;

import todo.core.FinStatus;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
/**
 *
 * @author Marcus Hertel
 */
public class FinStatusTableModel extends AbstractTableModel{
    
    /* Sitzungsarten-Objekte welche zeilenweise agezeigt werden sollen */
    protected ArrayList<FinStatus> finStatusObjects = new ArrayList<FinStatus>();
    private String[] columnNames = new String[2];
    private Vector colNam = new Vector();   //Zwischenspeicher für Array columnNames
    private static Connection con; 
    
    /** Creates a new instance of FinStatusTableModel */
    public FinStatusTableModel() {
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.finStatusObjects.get(zeile).getStatusID();
        case 1 :
            return this.finStatusObjects.get(zeile).getStatusName();
        default:
          return null;
        }
    }
    
    /*
     * return Anzahl der Sitzungsarten-Objekte
     */
    public int getRowCount() {
        return this.finStatusObjects.size();
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
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Status";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                int statusID = rst.getInt("StatusID");
                String statusName = rst.getString("Name");    
                finStatusObjects.add(new FinStatus(statusID, statusName));
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
        colNam.add("Nummer");
        colNam.add("Status");
        colNam.toArray(columnNames);
    }
}
