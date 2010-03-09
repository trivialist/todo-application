/*
 * AreaTableModel.java
 *
 * Created on 23. Juli 2007, 14:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package todo.tablemodel;

import todo.core.Area;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
/**
 *
 * @author Marcus Hertel
 */
public class AreaTableModel extends AbstractTableModel {
    
    /* Kategorieobjekte welche zeilenweise angezeigt werden sollen */
    protected ArrayList<Area> areaObjects = new ArrayList<Area>();
    private String[] columnNames = new String[3];
    private Vector colNam = new Vector();
    private static Connection con; 
    
    /** Creates a new instance of AreaTableModel */
    public AreaTableModel() {
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.areaObjects.get(zeile).getAreaID();
        case 1 :
            return this.areaObjects.get(zeile).getAreaName();
        case 2:
            return this.areaObjects.get(zeile).getAreaDescription();
        default:
          return null;
        }
    }
    
        
    /*
     * return Anzahl der Bereiche
     */
    public int getRowCount() {
        return this.areaObjects.size();
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
            String sql = "SELECT * FROM Bereich";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                int areaID = rst.getInt("BereichID");
                String areaName = rst.getString("Name");
                String areaDescription = rst.getString("Beschreibung");                
                areaObjects.add(new Area(areaID, areaName, areaDescription));
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
        colNam.add("Bereich");
        colNam.add("Beschreibung");
        colNam.toArray(columnNames);
    }
 
}
