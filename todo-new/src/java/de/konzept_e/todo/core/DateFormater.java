/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.core;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sven Skrabal
 */
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
