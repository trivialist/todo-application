/*
 * MeetingTableModel.java
 *
 * Created on 9. Januar 2007, 19:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.tablemodel;

import todo.core.Meeting;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.text.SimpleDateFormat;
/**
 *
 * @author Marcus Hertel
 */
public class MeetingTableModel extends AbstractTableModel{
    
    /* Sitzungdaten-Objekte welche zeilenweise agezeigt werden sollen */
    protected ArrayList<Meeting> meetingObjects = new ArrayList<Meeting>();
    private String[] columnNames = new String[3];
    private Vector colNam = new Vector();   //Zwischenspeicher für Array columnNames
    private static Connection con;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    /** Creates a new instance of MeetingTableModel */
    public MeetingTableModel() {
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return sdf.format(this.meetingObjects.get(zeile).getDate());
        case 1 :
            return this.meetingObjects.get(zeile).getPlace();
        case 2 :
            return this.meetingObjects.get(zeile).getMeetingType();
        case -1 :
            return this.meetingObjects.get(zeile).getMeetingID();
        default:
          return null;
        }
    }
    
    /*
     * return Anzahl der Sitzungsarten-Objekte
     */
    public int getRowCount() {
        return this.meetingObjects.size();
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
            String sql = "SELECT * FROM Sitzungsdaten ORDER BY Datum DESC";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                Date date = rst.getDate("Datum");
                String place = rst.getString("Ort");
                int meetingID = rst.getInt("SitzungsdatenID");
                int meetingTypeID = rst.getInt("SitzungsartID");
                String meetingType = "";
                Statement stmt2 = con.createStatement();
                String sql2 = "SELECT * FROM Sitzungsart WHERE SitzungsartID = " +
                              meetingTypeID;
                ResultSet rst2 = stmt2.executeQuery(sql2);
                
                while(rst2.next()) {
                    meetingType = rst2.getString("Name");
                }
                meetingObjects.add(new Meeting(date, place, meetingType, meetingID));
                rst2.close();
                stmt2.close();
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
        colNam.add("Datum");
        colNam.add("Ort");
        colNam.add("Sitzungsart");
        colNam.toArray(columnNames);
    }
    
}
