/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import todo.dbcon.drivers.MsAccessDriver;
import todo.gui.MainGUI;

/**
 * @author sven
 */
public class DbTest
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			FileInputStream inputStream = new FileInputStream(new File("./ToDoAppSettings.xml"));
			MainGUI.applicationProperties.loadFromXML(inputStream);
			inputStream.close();

		} catch (Exception ex)
		{
			Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
		}

		DB_ToDo_Connect.openDB();
		DbStorage dbs = new DbStorage();
		dbs.setDbDriver(new MsAccessDriver());
		dbs.setDebugEnabled(true);
		dbs.setDatabaseConnection(DB_ToDo_Connect.getCon());

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("Name", "neu");
		ForeignElement fe = (ForeignElement) dbs.loadFirst(new ForeignElement(), hm);
		BaseElement be = new BaseElement();
		be.name = "base";
		be.element = fe;

		dbs.insert(be);
		/*dbs.update(m);
		dbs.delete(m);

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("MemoID", 8);
		hm.put("comment", "bbb");
		LinkedList<Object> x = dbs.load(new Memo(), hm);


		for (Object c : x)
		{
		Memo tmp = (Memo) c;
		System.out.println(tmp.getComment());
		}
		 */
	}
}
