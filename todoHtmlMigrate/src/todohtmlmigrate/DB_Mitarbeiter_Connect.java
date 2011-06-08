/*
 * DB_Mitarbeiter_Connect.java
 *
 * Created on 22. November 2006, 13:27
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package todohtmlmigrate;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hertel
 */
public class DB_Mitarbeiter_Connect
{
	private static Connection con = null;

	/** Creates a new instance of DB_Mitarbeiter_Connect */
	public DB_Mitarbeiter_Connect()
	{
	}

	public static void openDB()
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();

			String url = "jdbc:odbc:personal_db";
			con = DriverManager.getConnection(url);
		} catch (Exception ex)
		{
			Logger.getLogger(DB_Mitarbeiter_Connect.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static void closeDB(Connection con)
	{
		try
		{
			con.close();
		} catch (Exception ex)
		{
			Logger.getLogger(DB_Mitarbeiter_Connect.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static Connection getCon()
	{
		return con;
	}
}
