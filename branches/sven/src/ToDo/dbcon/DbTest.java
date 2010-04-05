/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import todo.dbcon.drivers.MsAccessDriver;

/**
 * @author sven
 */
public class DbTest
{

	public static void main(String[] args) throws Exception
	{
		DB_ToDo_Connect.openDB();

		DbStorage dbs = new DbStorage();
		dbs.setDbDriver(new MsAccessDriver());
		dbs.setDebugEnabled(true);
		dbs.setDatabaseConnection(DB_ToDo_Connect.getCon());

		ForeignElement fe = new ForeignElement();
		fe.name = "neu";
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
