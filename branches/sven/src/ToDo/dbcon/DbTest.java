/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import todo.dbcon.drivers.MsAccessDriver;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import todo.core.Memo;

/**
 * @author sven
 */
public class DbTest
{

	public static void main(String[] args) throws Exception
	{
		DbStorage dbs = new DbStorage();
		dbs.setDbDriver(new MsAccessDriver());
		dbs.setDebugEnabled(true);
		Memo m = new Memo();

		m.setComment("xxx");
		m.setDate(new Date());
		m.setMemoID(2);
		m.setTodoID(7);
		m.setUser("sdfsdf");

		dbs.insert(m);
		dbs.update(m);
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
	}
}
