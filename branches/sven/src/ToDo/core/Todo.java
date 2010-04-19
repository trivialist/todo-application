/*
 * Todo.java
 *
 * Created on 10. Januar 2007, 05:28
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
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
@DbTable(name = "Protokollelement")
public class Todo
{

	@DbId(name = "ToDoID")
	private int todoID;
	@DbColumn(name = "KategorieID")
	private int categoryID;
	@DbColumn(name = "SitzungsID")
	private int meetingID;
	@DbColumn(name = "StatusID")
	private int statusID;
	@DbColumn(name = "InstitutionID")
	private int institutionID;
	@DbColumn(name = "TBZuordnung_ID")
	private int tbz_id;
	@DbColumn(name = "BereichID")
	private int areaID;
	@DbColumn(name = "Bereich")
	private String area;
	@DbColumn(name = "Thema")
	private String topic;
	@DbColumn(name = "Inhalt")
	private String content;
	@DbColumn(name = "Wiedervorlagedatum")
	private Date reDate;
	@DbColumn(name = "WV_Sitzungsart")
	private int reMeetType;
	@DbColumn(name = "Verantwortliche")
	private String responsible;
	@DbColumn(name = "Beteiligte")
	private String involved;
	@DbColumn(name = "Überschrift")
	private String heading;
	@DbColumn(name = "WiedervorlageGesetzt")
	private boolean reMeetingEnabled;
	@DbColumn(name = "updated")
	private boolean updated;
	@DbColumn(name = "Geloescht")
	private boolean deleted;
	@DbColumn(name = "Kopiergrund")
	private String copyReason;
	//unknown
	public String category;
	public String status;
	public int topicID;

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

	public String getRespons()
	{
		return responsible;
	}

	public String getOthers()
	{
		return involved;
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

	public void setResponse(String responsible)
	{
		this.responsible = responsible;
	}

	public void setOthers(String involved)
	{
		this.involved = involved;
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
		responsible = "";
		involved = "";
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
}
