/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.core;

import java.util.Date;
import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbTable;

/**
 *
 * @author Marcus Hertel
 */
@DbTable(name = "Memo")
public class Memo
{

	@DbId(name = "MemoID")
	private int memoID;
	@DbColumn(name = "TodoID")
	private int todoID;
	@DbColumn(name = "Inhalt")
	private String comment;
	@DbColumn(name = "erstellt")
	private Date memoDate;
	@DbColumn(name = "Benutzer")
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
