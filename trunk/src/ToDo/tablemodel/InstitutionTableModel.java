/*
 * InstitutionTableModel.java
 *
 * Created on 29. Dezember 2006, 18:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.tablemodel;

import todo.core.Institution;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
/**
 *
 * @author Marcus Hertel
 */
public class InstitutionTableModel extends AbstractTableModel{
    
    /* Institutionen-Objekte welche zeilenweise agezeigt werden sollen */
    protected ArrayList<Institution> institutionObjects = new ArrayList<Institution>();
    private String[] columnNames = new String[2];
    private Vector colNam = new Vector();   //Zwischenspeicher f�r Array columnNames
    private static Connection con; 
    
    /** Creates a new instance of InstitutionTableModel */
    public InstitutionTableModel() {
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.institutionObjects.get(zeile).getInstitutionID();
        case 1 :
            return this.institutionObjects.get(zeile).getInstitution();
        default:
          return null;
        }
    }
    
    /*
     * return Anzahl der Sitzungsarten-Objekte
     */
    public int getRowCount() {
        return this.institutionObjects.size();
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
            String sql = "SELECT * FROM Institution";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                int institutionID = rst.getInt("InstitutionID");
                String institution = rst.getString("Name");    
                institutionObjects.add(new Institution(institutionID, institution));
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
        colNam.add("Institution");
        colNam.toArray(columnNames);
    }
    
}
