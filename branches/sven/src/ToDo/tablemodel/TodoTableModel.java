/*
 * TodoTableModel.java
 *
 * Created on 10. Januar 2007, 05:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.tablemodel;

import todo.core.Todo;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Marcus Hertel
 */
public class TodoTableModel extends AbstractTableModel{
    
    /* Todo-Objekte welche zeilenweise agezeigt werden sollen */
    protected ArrayList<Todo> todoObjects = new ArrayList<Todo>();
    private String[] columnNames = new String[6];
    private Vector colNam = new Vector();   //Zwischenspeicher für Array columnNames
    private static Connection con; 
    private static int meetingID;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    /** Creates a new instance of TodoTableModel */
    public TodoTableModel(int meetingID) {
        this.meetingID = meetingID;
        this.loadData();
    }
    
    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.todoObjects.get(zeile).getCategory();
        case 1 :
            return this.todoObjects.get(zeile).getArea();
        case 2 :
            return this.todoObjects.get(zeile).getTopic();
        case 3 :
            Date reDate = this.todoObjects.get(zeile).getReDate();
            if(reDate == null) {
                return "kein";
            } else {
                return sdf.format(reDate);
            }
        case 4:
            return this.todoObjects.get(zeile).getTodoID();
		case 5:
			return this.todoObjects.get(zeile).getHeading();
        default:
          return null;
        }
    }
    
    /*
     * return Anzahl der Todo-Objekte
     */
    public int getRowCount() {
        return this.todoObjects.size();
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
        Todo td = new Todo();
        
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Protokollelement WHERE SitzungsID=" + meetingID;
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                td.clear();
                int tbz_id = -1;
                td.setCategoryID(rst.getInt("KategorieID"));
                td.setCategory(getCategoryByID(td.getCategoryID()));
                tbz_id = rst.getInt("TBZuordnung_ID");
                td.setTBZ_ID(tbz_id);
                td.setArea(getAreaNameByTBZ_ID(tbz_id));
                td.setTopic(getTopicNameByTBZ_ID(tbz_id));
                td.setReDate(rst.getDate("Wiedervorlagedatum"));
                td.setTodoID(rst.getInt("ToDoID"));
				td.setHeading(rst.getString("Überschrift"));
                todoObjects.add(new Todo(td.getCategoryID(), td.getCategory(),
                                         td.getTBZ_ID(), td.getArea(),
                                         td.getTopic(), td.getReDate(),
                                         td.getTodoID(), td.getHeading()));
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
        colNam.add("Kategorie");
        colNam.add("Bereich");
        colNam.add("Thema");
        colNam.add("Wiedervorlage");
        colNam.add("Nummer");
		colNam.add("Überschrift");
        colNam.toArray(columnNames);
    }
    
    public String getCategoryByID(int catID) {
        String cat = "";
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();
        
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Kategorie WHERE KategorieID = " + catID;
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                cat = rst.getString("Name");
            }
            rst.close();
            stmt.close();
        }
        catch(Exception e) {
            System.out.println(e.toString()); 
            System.exit(1); 
        }
        dbCon.closeDB(con);
        return cat;
    }

    public String getTopicNameByTBZ_ID(int tbz_id) {
        String name = "";
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();

        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM TBZ INNER JOIN Thema ON TBZ.ThemaID=Thema.ThemaID  " +
                         "WHERE TBZ_ID=" + tbz_id;
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                name = rst.getString("Name");
            }
            rst.close();
            stmt.close();
        }
        catch(Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }
        dbCon.closeDB(con);
        return name;
    }

    public String getAreaNameByTBZ_ID(int tbz_id) {
        String name = "";
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();

        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM TBZ INNER JOIN Bereich ON TBZ.BereichID=Bereich.BereichID " +
                         "WHERE TBZ_ID=" + tbz_id;
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                name = rst.getString("Name");
            }
            rst.close();
            stmt.close();
        }
        catch(Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }
        dbCon.closeDB(con);
        return name;
    }

    
}
