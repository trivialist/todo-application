/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e f�r Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.entity;

import java.util.Date;

public class Memo
{
	private int memoID;
	private int todoID;
	private String comment;
	private Date memoDate;
	private String memoUser;

	public Memo()
	{
	}

	public Memo(int todoID, String comment)
	{
		this.todoID = todoID;
		this.comment = comment;
	}

	public int getMemoID()
	{
		return memoID;
	}

	public int getTodoID()
	{
		return todoID;
	}

	public String getComment()
	{
		return comment;
	}

	public Date getDate()
	{
		return memoDate;
	}

	public String getUser()
	{
		return memoUser;
	}

	public void setMemoID(int memoID)
	{
		this.memoID = memoID;
	}

	public void setTodoID(int todoID)
	{
		this.todoID = todoID;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setDate(Date memoTimestamp)
	{
		this.memoDate = memoTimestamp;
	}

	public void setUser(String winUser)
	{
		this.memoUser = winUser;
	}
}
