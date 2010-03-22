/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.core;

import java.util.Date;
import todo.dbcon.DbColumn;
import todo.dbcon.DbEntity;
import todo.dbcon.DbId;
import todo.dbcon.DbTable;

/**
 *
 * @author Marcus Hertel
 */
@DbEntity
@DbTable(name="Memo")
public class Memo
{
	@DbId(name="MemoID")
	private int memoID;
	@DbColumn(name="TodoID")
	private int todoID;
	@DbColumn(name="Inhalt")
	private String comment;
	@DbColumn(name="erstellt")
	private Date memoDate;
	@DbColumn(name="Benutzer")
	private String memoUser;
	@DbColumn(name="booldb")
	private boolean bool;
	@DbColumn(name="floatdb")
	private float fl;
	@DbColumn(name="doubledb")
	private double dbl;
	@DbColumn(name="longdb")
	private long lng;

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
