/*
 * Meeting.java
 *
 * Created on 6. Januar 2007, 16:49
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package todo.core;

import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author Marcus Hertel
 */
public class Meeting
{
	private int meetingID;
	private Date date;
	private String place;
	private int prot;
	private int meetingTypeID;
	private String otherPart;
	private int recorder;
	private boolean deleted;
	private String meetingType;

	/** Creates a new instance of Meeting */
	public Meeting()
	{
	}

	public Meeting(Date date, String place, String meetingType, int meetingID)
	{
		this.date = date;
		this.place = place;
		this.meetingType = meetingType;
		this.meetingID = meetingID;
	}

	public int getMeetingID()
	{
		return meetingID;
	}

	public int getProt()
	{
		return prot;
	}

	public Date getDate()
	{
		return date;
	}

	public String getPlace()
	{
		return place;
	}

	public int getMeetingTypeID()
	{
		return meetingTypeID;
	}

	public String getMeetingType()
	{
		return meetingType;
	}

	public String getOtherPaticipants()
	{
		return otherPart;
	}

	public void setMeetingID(int meetingID)
	{
		this.meetingID = meetingID;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setPlace(String place)
	{
		this.place = place;
	}

	public void setProt(int prot)
	{
		this.prot = prot;
	}

	public void setMeetingTypeID(int meetingTypeID)
	{
		this.meetingTypeID = meetingTypeID;
	}

	public void setMeetingType(String meetingType)
	{
		this.meetingType = meetingType;
	}

	public void setOtherParticipants(String otherPart)
	{
		this.otherPart = otherPart;
	}

	public void clear()
	{
		setDate(Calendar.getInstance().getTime());
		setMeetingTypeID(0);
		setPlace("");
	}

	public int getRecorder()
	{
		return recorder;
	}

	public void setRecorder(int recorder)
	{
		this.recorder = recorder;
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
