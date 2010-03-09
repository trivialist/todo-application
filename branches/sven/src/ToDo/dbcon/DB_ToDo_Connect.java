/*
 * DB_ToDo_Connect.java
 *
 * Created on 22. November 2006, 13:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.dbcon;


import java.awt.*; 
import java.awt.event.*; 
import java.sql.*; 

/**
 *
 * @author hertel
 */
public class DB_ToDo_Connect {
    
    private static Connection con = null;
    
    /** Creates a new instance of DB_ToDo_Connect */
    public DB_ToDo_Connect() {
    }
    
    
    public static void openDB() {
        try { 
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance(); 
          
            String url = "jdbc:odbc:todo";
            con = DriverManager.getConnection(url);
        }
        catch (Exception ex) 
        { 
          System.out.println(ex.toString()); 
          System.exit(1); 
        }
        
    }
        
    public static void closeDB(Connection con) {
        try {
            con.close(); 
        } 
        catch (Exception ex) 
        { 
          System.out.println(ex.toString()); 
          System.exit(1); 
        } 
    }
    
    public static Connection getCon() {
        return con;
    }
    
}


