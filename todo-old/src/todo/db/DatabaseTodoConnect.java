/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.db;

import todo.util.GlobalError;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import todo.gui.MainGui;

public class DatabaseTodoConnect
{
	private static Connection con = null;

	public static Connection openDB()
	{
		try
		{
			Class.forName(MainGui.applicationProperties.getProperty("DatabaseDriver")).newInstance();
			String url = MainGui.applicationProperties.getProperty("DatabaseDsnTodo");
			con = DriverManager.getConnection(url);

			return con;
		}
		catch (Exception ex)
		{
			Logger.getLogger(DatabaseTodoConnect.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}

		return null;
	}

	public static void closeDB(Connection con)
	{
		try
		{
			con.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(DatabaseTodoConnect.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
	}
}
