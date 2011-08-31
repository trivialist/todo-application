/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.entity;

import java.util.ArrayList;
import java.util.Date;

public class Todo
{
	private int todoID;
	private int categoryID;
	private int meetingID;
	private int statusID;
	private int institutionID;
	private int tbz_id;
	private int areaID;
	private String area;
	private String topic;
	private String content;
	private Date reDate;
	private int reMeetType;
	private String heading;
	private boolean reMeetingEnabled;
	private boolean updated;
	private boolean deleted;
	private ArrayList<Integer> responsible;
	private ArrayList<Integer> involved;
	private String copyReason;
	public String category;
	public String status;
	public int topicID;
	private String reMeetingType;

	/** Creates a new instance of Todo */
	public Todo()
	{
	}

	public Todo(String category, int tbz_id, Date reDate, int todoID)
	{
		this.category = category;
		this.tbz_id = tbz_id;
		this.reDate = reDate;
		this.todoID = todoID;
	}

	public Todo(int categoryID, String category, int tbz_id, String area, String topic, Date reDate, int todoID, String heading, String content, boolean enabled)
	{
		this.categoryID = categoryID;
		this.category = category;
		this.tbz_id = tbz_id;
		this.area = area;
		this.topic = topic;
		this.reDate = reDate;
		this.todoID = todoID;
		this.heading = heading;
		this.content = content;
		this.reMeetingEnabled = enabled;
	}

	public boolean getReMeetingEnabled()
	{
		return reMeetingEnabled;
	}

	public int getTodoID()
	{
		return todoID;
	}

	public int getCategoryID()
	{
		return categoryID;
	}

	public int getMeetingID()
	{
		return meetingID;
	}

	public int getStatusID()
	{
		return statusID;
	}

	public int getInstitutionID()
	{
		return institutionID;
	}

	public int getTBZ_ID()
	{
		return tbz_id;
	}

	public int getAreaID()
	{
		return areaID;
	}

	public int getTopicID()
	{
		return topicID;
	}

	public String getArea()
	{
		return area;
	}

	public String getTopic()
	{
		return topic;
	}

	public String getContent()
	{
		return content;
	}

	public Date getReDate()
	{
		return reDate;
	}

	public int getReMeetType()
	{
		return reMeetType;
	}

	public String getCategory()
	{
		return category;
	}

	public String getStatus()
	{
		return status;
	}

	public String getHeading()
	{
		return heading;
	}

	public void setReMeetingEnabled(boolean status)
	{
		reMeetingEnabled = status;
	}

	public void setTodoID(int todoID)
	{
		this.todoID = todoID;
	}

	public void setCategoryID(int categoryID)
	{
		this.categoryID = categoryID;
	}

	public void setMeetingID(int meetingID)
	{
		this.meetingID = meetingID;
	}

	public void setStatusID(int statusID)
	{
		this.statusID = statusID;
	}

	public void setInstitutionID(int institutionID)
	{
		this.institutionID = institutionID;
	}

	public void setTBZ_ID(int tbz_id)
	{
		this.tbz_id = tbz_id;
	}

	public void setAreaID(int areaID)
	{
		this.areaID = areaID;
	}

	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public void setReDate(Date reDate)
	{
		this.reDate = reDate;
	}

	public void setReMeetType(int reMeetType)
	{
		this.reMeetType = reMeetType;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public void setHeading(String heading)
	{
		this.heading = heading;
	}

	public void clear()
	{
		todoID = 0;
		categoryID = 0;
		meetingID = 0;
		statusID = 0;
		institutionID = 0;
		tbz_id = 0;
		area = "";
		topic = "";
		content = "";
		reDate = new Date();
		category = "";
		status = "";
		heading = "";
	}

	public String getCopyReason()
	{
		return copyReason;
	}

	public void setCopyReason(String copyReason)
	{
		this.copyReason = copyReason;
	}

	public boolean isUpdated()
	{
		return updated;
	}

	public void setUpdated(boolean updated)
	{
		this.updated = updated;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public String getReMeetingType()
	{
		return reMeetingType;
	}

	public void setReMeetingType(String reMeetingType)
	{
		this.reMeetingType = reMeetingType;
	}

	public ArrayList<Integer> getResponsible()
	{
		return responsible;
	}

	public void setResponsible(ArrayList<Integer> responsible)
	{
		this.responsible = responsible;
	}

	public ArrayList<Integer> getInvolved()
	{
		return involved;
	}

	public void setInvolved(ArrayList<Integer> involved)
	{
		this.involved = involved;
	}
}
