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

import todo.gui.GlobalError;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import todo.gui.MainGUI;

/**
 *
 * @author hertel
 */
public class DB_ToDo_Connect
{

	private static Connection con = null;

	/** Creates a new instance of DB_ToDo_Connect */
	public DB_ToDo_Connect()
	{
	}

	public static void openDB()
	{
		try
		{
			Class.forName(MainGUI.applicationProperties.getProperty("DatabaseDriver")).newInstance();

			String url = MainGUI.applicationProperties.getProperty("DatabaseDsnTodo");
			con = DriverManager.getConnection(url);
		} catch (Exception ex)
		{
			Logger.getLogger(DB_ToDo_Connect.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

	}

	public static void closeDB(Connection con)
	{
		try
		{
			con.close();
		} catch (Exception ex)
		{
			Logger.getLogger(DB_ToDo_Connect.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
	}

	public static Connection getCon()
	{
		return con;
	}
}


