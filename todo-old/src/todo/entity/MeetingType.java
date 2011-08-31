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

public class MeetingType
{
	private String meetingType;
	private int meetingTypeID;

	/** Creates a new instance of MeetingType */
	public MeetingType()
	{
	}

	public MeetingType(String meetingType)
	{
		this.meetingType = meetingType;
	}

	public MeetingType(int meetingTypeID, String meetingType)
	{
		this.meetingTypeID = meetingTypeID;
		this.meetingType = meetingType;
	}

	public String getMeetingType()
	{
		return meetingType;
	}

	public void setMeetingType(String meetingType)
	{
		this.meetingType = meetingType;
	}

	public int getMeetingTypeID()
	{
		return meetingTypeID;
	}

	public void setMeetingTypeID(int meetingTypeID)
	{
		this.meetingTypeID = meetingTypeID;
	}
}
