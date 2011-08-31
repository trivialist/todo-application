/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater implements Comparable
{

	private Date displayDate;
	private boolean dateNotSetFlag;
	private SimpleDateFormat dateFormatterInstance = new SimpleDateFormat("dd.MM.yyyy");

	public DateFormater(Date displayDate, boolean dateNotSetFlag)
	{
		this.displayDate = displayDate;
		this.dateNotSetFlag = dateNotSetFlag;
	}

	public DateFormater(Date displayDate)
	{
		this.displayDate = displayDate;
		this.dateNotSetFlag = true;
	}

	public Date getDate()
	{
		return displayDate;
	}

	@Override
	public String toString()
	{
		if (!dateNotSetFlag)
		{
			return "kein";
		}

		return dateFormatterInstance.format(displayDate);
	}

	/**
	 * @see Javadoc http://java.sun.com/javase/6/docs/api/java/lang/Comparable.html
	 * 
	 * @param compareObject	Object to be compared
	 * @return -1, 0, 1
	 */
	@Override
	public int compareTo(Object compareObject)
	{
		DateFormater dateCompareObject = (DateFormater) compareObject;

		if (!dateNotSetFlag)
		{
			if (!dateCompareObject.dateNotSetFlag)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}

		if (!dateCompareObject.dateNotSetFlag)
		{
			return -1;
		}

		return displayDate.compareTo(dateCompareObject.displayDate);
	}
}
