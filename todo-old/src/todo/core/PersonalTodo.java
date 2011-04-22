/*
 * PersonalTodo.java
 *
 * Created on 4. November 2007, 00:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package todo.core;

import java.util.ArrayList;

/**
 *
 * @author Marcus Hertel
 */
public class PersonalTodo
{
	private int todoID;
	private int categoryID;
	private int areaID;
	private int institutionID;
	private int statusID;
	private String topic;
	private String content;
	private String reDate;
	private ArrayList<Integer> responsible;
	private ArrayList<Integer> involved;
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

	public ArrayList<Integer> getResponsible()
	{
		return responsible;
	}

	public ArrayList<Integer> getInvolved()
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

	public void setResponsible(ArrayList<Integer> responsible)
	{
		this.responsible = responsible;
	}

	public void setInvolved(ArrayList<Integer> involved)
	{
		this.involved = involved;
	}

	public void setMeetingID(int meetingID)
	{
		this.meetingID = meetingID;
	}
}
