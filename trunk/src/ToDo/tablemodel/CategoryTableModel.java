/*
 * CategoryTableModel.java
 *
 * Created on 30. Dezember 2006, 20:47
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.tablemodel;

import todo.core.Category;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author Marcus Hertel
 */
public class CategoryTableModel extends AbstractTableModel{
    
    /* Kategorieobjekte welche zeilenweise angezeigt werden sollen */
    protected ArrayList<Category> catObjects = new ArrayList<Category>();
    private String[] columnNames = new String[3];
    private Vector colNam = new Vector();
    private static Connection con; 
    
    /** Creates a new instance of CategoryTableModel */
    public CategoryTableModel() {
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.catObjects.get(zeile).getCatID();
        case 1 :
            return this.catObjects.get(zeile).getCatName();
        case 2:
            return this.catObjects.get(zeile).getCatDescription();
        default:
          return null;
        }
    }
    
    
    
    /*
     * return Anzahl der Kategorien
     */
    public int getRowCount() {
        return this.catObjects.size();
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
            String sql = "SELECT * FROM Kategorie";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                int catID = rst.getInt("KategorieID");
                String catName = rst.getString("Name");
                String catDescription = rst.getString("Beschreibung");                
                catObjects.add(new Category(catID, catName, catDescription));
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
        colNam.add("Kategorie");
        colNam.add("Beschreibung");
        colNam.toArray(columnNames);
    }
    
}
