/*
 * PersonalTodo.java
 *
 * Created on 4. November 2007, 00:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package todo.core;

import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbTable;

/**
 *
 * @author Marcus Hertel
 */
@DbTable(name = "Protokollelement")
public class PersonalTodo
{

	@DbId(name = "ToDoID")
	private int todoID;
	@DbColumn(name = "KategorieID")
	private int categoryID;
	@DbColumn(name = "BereichID")
	private int areaID;
	@DbColumn(name = "InstitutionsID")
	private int institutionID;
	@DbColumn(name = "StatusID")
	private int statusID;
	@DbColumn(name = "Thema")
	private String topic;
	@DbColumn(name = "Inhalt")
	private String content;
	@DbColumn(name = "Wiedervorlagedatum")
	private String reDate;
	@DbColumn(name = "Verantwortliche")
	private String responsible;
	@DbColumn(name = "Beteiligte")
	private String involved;
	@DbColumn(name = "SitzungsID")
	private int meetingID;

	/** Creates a new instance of PersonalTodo */
	public PersonalTodo()
	{
	}

	/** GET - Methoden */
	public int getTodoID()
	{
		return todoID;
	}

	public int getCategoryID()
	{
		return categoryID;
	}

	public int getAreaID()
	{
		return areaID;
	}

	public int getInstitutionID()
	{
		return institutionID;
	}

	public int getStatusID()
	{
		return statusID;
	}

	public String getTopic()
	{
		return topic;
	}

	public String getContent()
	{
		return content;
	}

	public String getReDate()
	{
		return reDate;
	}

	public String getResponsible()
	{
		return responsible;
	}

	public String getInvolved()
	{
		return involved;
	}

	public int getMeetingID()
	{
		return meetingID;
	}

	/** SET - Methoden */
	public void setTodoID(int todoID)
	{
		this.todoID = todoID;
	}

	public void setCategoryID(int categoryID)
	{
		this.categoryID = categoryID;
	}

	public void setAreaID(int areaID)
	{
		this.areaID = areaID;
	}

	public void setInstitutionID(int institutionID)
	{
		this.institutionID = institutionID;
	}

	public void setStatusID(int statusID)
	{
		this.statusID = statusID;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public void setReDate(String reDate)
	{
		this.reDate = reDate;
	}

	public void setResponsible(String responsible)
	{
		this.responsible = responsible;
	}

	public void setInvolved(String involved)
	{
		this.involved = involved;
	}

	public void setMeetingID(int meetingID)
	{
		this.meetingID = meetingID;
	}
}
