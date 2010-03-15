/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.tablemodel;

import todo.core.Topic;
import todo.dbcon.DB_ToDo_Connect;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author Marcus Hertel
 */
public class TopicTableModel extends AbstractTableModel {

    /* Themenobjekte welche zeilenweise angezeigt werden sollen */
    protected ArrayList<Topic> topicObjects = new ArrayList<Topic>();
    private String[] columnNames = new String[2];
    private Vector colNam = new Vector();
    private static Connection con;

    /** Creates a new instance of AreaTableModel */
    public TopicTableModel() {
        this.loadData();
    }

    public Object getValueAt(final int zeile, final int spalte) {
        switch (spalte) {
        case -1 :
            return this.topicObjects.get(zeile).getTopicID();
        case 0 :
            return this.topicObjects.get(zeile).getName();
        case 1:
            return this.topicObjects.get(zeile).getDescription();
        default:
          return null;
        }
    }


    /*
     * return Anzahl der Bereiche
     */
    public int getRowCount() {
        return this.topicObjects.size();
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
            String sql = "SELECT * FROM Thema";
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                int topicID = rst.getInt("ThemaID");
                String name = rst.getString("Name");
                String description = rst.getString("Beschreibung");
                topicObjects.add(new Topic(topicID, name, description));
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
        colNam.add("Bereich");
        colNam.add("Beschreibung");
        colNam.toArray(columnNames);
    }
}
