/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.tablemodel;

import todo.core.Todo;
import todo.core.Employee;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;
import java.text.SimpleDateFormat;
import javax.swing.*;
/**
 *
 * @author Marcus Hertel
 */
public class PersonalTodoTableModel extends AbstractTableModel{
    /* Sitzungdaten-Objekte welche zeilenweise agezeigt werden sollen */
    protected ArrayList<Todo> ptdObjects = new ArrayList<Todo>();
    private String[] columnNames = new String[6];
    private Vector colNam = new Vector();   //Zwischenspeicher f�r Array columnNames
    private static Connection con;
    private int status = -1;
    private String sStat = ""; //falls als status -1 �bergeben wird, wurde Status="Alle" gew�hlt
                               //in Sql-Abfrage sollen also alle Datens�tze ermittelt werden -> kein Status in where-klausel
    private Employee emp = new Employee();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    /** Creates a new instance of MeetingTableModel */
    public PersonalTodoTableModel(int employee, int stat) {
        emp.setEmployeeID(employee);
        this.status = stat;
        if(emp.getEmployeeID() == -1) {
            if(status != -1) {
                if(status != 0) {
                    sStat = " WHERE Protokollelement.StatusID=" + status;
                }
            }
            this.loadOpData();
        } else {
            if(status != -1) {
                if(status != 0) {
                    sStat = " AND Protokollelement.StatusID=" + status;
                }
            }
            this.loadData();
        }
    }

    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case 0 :
            return this.ptdObjects.get(zeile).getTodoID();
        case 1 :
            return this.ptdObjects.get(zeile).getTopic();
        case 2 :
            return this.ptdObjects.get(zeile).getCategory();
        case 3 :
            if(this.ptdObjects.get(zeile).getReDate() != null) {
                return sdf.format(this.ptdObjects.get(zeile).getReDate());
            } else {
                return "kein";
            }
        case 4 :
            return this.ptdObjects.get(zeile).getStatus();
        case 5 :
            return this.ptdObjects.get(zeile).getContent();
        default:
          return null;
        }
    }

    /*
     * return Anzahl der Sitzungsarten-Objekte
     */
    public int getRowCount() {
        return this.ptdObjects.size();
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
            String sql = "SELECT Protokollelement.ToDoID as ToDoID, Thema.Name as Thema, Kategorie.Name as Kategorie, " +
                    "Protokollelement.Wiedervorlagedatum as WV, Protokollelement.Inhalt as Inhalt, Status.Name as Status " +
                    "FROM Kategorie INNER JOIN " +
                    "((Thema INNER JOIN TBZ ON Thema.ThemaID = TBZ.ThemaID) " +
                    "INNER JOIN (Status INNER JOIN Protokollelement ON Status.StatusID = Protokollelement.StatusID) " +
                    "ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON Kategorie.KategorieID = Protokollelement.KategorieID " +
                    "WHERE Protokollelement.Verantwortliche " +
                    "LIKE '%," + emp.getEmployeeID() + ",%' " + sStat +
                    " OR Protokollelement.Verantwortliche LIKE '%," + emp.getEmployeeID() + "%'" + sStat;
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                Todo td = new Todo();
                java.util.Date rd = new java.util.Date();
                td.setTodoID(rst.getInt("ToDoID"));
                td.setTopic(rst.getString("Thema"));
                td.setCategory(rst.getString("Kategorie"));
                rd = rst.getDate("WV");
                if(rd != null) {
                    td.setReDate(rd);
                }
                td.setContent(rst.getString("Inhalt"));
                td.setStatus(rst.getString("Status"));
                ptdObjects.add(td);
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

    protected void loadOpData() {
        DB_ToDo_Connect dbCon = new DB_ToDo_Connect();
        dbCon.openDB();
        con = dbCon.getCon();

        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT Protokollelement.ToDoID as ToDoID, Thema.Name as Thema, Kategorie.Name as Kategorie, " +
                    "Protokollelement.Wiedervorlagedatum as WV, Protokollelement.Inhalt as Inhalt, Status.Name as Status " +
                    "FROM Kategorie INNER JOIN " +
                    "((Thema INNER JOIN TBZ ON Thema.ThemaID = TBZ.ThemaID) " +
                    "INNER JOIN (Status INNER JOIN Protokollelement ON Status.StatusID = Protokollelement.StatusID) " +
                    "ON TBZ.TBZ_ID = Protokollelement.TBZuordnung_ID) ON Kategorie.KategorieID = Protokollelement.KategorieID " +
                    sStat;
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                Todo td = new Todo();
                java.util.Date rd = new java.util.Date();
                td.setTodoID(rst.getInt("ToDoID"));
                td.setTopic(rst.getString("Thema"));
                td.setCategory(rst.getString("Kategorie"));
                rd = rst.getDate("WV");
                if(rd != null) {
                    td.setReDate(rd);
                }
                td.setContent(rst.getString("Inhalt"));
                td.setStatus(rst.getString("Status"));
                ptdObjects.add(td);
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
        colNam.add("ID");
        colNam.add("Thema");
        colNam.add("Kategorie");
        colNam.add("Wiedervorlage");
        colNam.add("Status");
        colNam.add("Inhalt");
        colNam.toArray(columnNames);
    }

}
