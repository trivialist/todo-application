/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.tablemodel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sven
 */
public class DateFormater implements Comparable
{
	private Date d;
	private boolean flag;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	public DateFormater(Date x, boolean flag)
	{
		d = x;
		this.flag = flag;
	}

	public DateFormater(Date x)
	{
		d = x;
		this.flag = true;
	}

	public Date getDate()
	{
		return d;
	}

	public String toString()
	{
		if(!flag)
		{
			return "keins";
		}

		return sdf.format(d);
	}

	public int compareTo(Object o)
	{
		DateFormater x = (DateFormater) o;

		if(!flag)
		{
			if(!x.flag)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}

		if(!x.flag)
		{
			return -1;
		}

		return d.compareTo(x.d);
	}
}
