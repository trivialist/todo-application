/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.konzept_e.todo.database.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.criteria.Fetch;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "meeting")
public class Meeting implements Serializable
{
	@Id
	@Column(name = "meetingId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date date;

	@Column(name = "place")
	private String place;

	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "others")
	private String otherParticipiants;

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "meetingTypeId")
	private MeetingType meetingType;

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "minuteTakerId")
	private Employee minuteTaker;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable( name="meeting_employee",
				joinColumns=@JoinColumn(name="meetingId"),
				inverseJoinColumns=@JoinColumn(name="employeeId"))
	private List<Employee> participiants;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * @return the place
	 */
	public String getPlace()
	{
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place)
	{
		this.place = place;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted()
	{
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	/**
	 * @return the otherParticipiants
	 */
	public String getOtherParticipiants()
	{
		return otherParticipiants;
	}

	/**
	 * @param otherParticipiants the otherParticipiants to set
	 */
	public void setOtherParticipiants(String otherParticipiants)
	{
		this.otherParticipiants = otherParticipiants;
	}

	/**
	 * @return the meetingType
	 */
	public MeetingType getMeetingType()
	{
		return meetingType;
	}

	/**
	 * @param meetingType the meetingType to set
	 */
	public void setMeetingType(MeetingType meetingType)
	{
		this.meetingType = meetingType;
	}

	/**
	 * @return the minuteTaker
	 */
	public Employee getMinuteTaker()
	{
		return minuteTaker;
	}

	/**
	 * @param minuteTaker the minuteTaker to set
	 */
	public void setMinuteTaker(Employee minuteTaker)
	{
		this.minuteTaker = minuteTaker;
	}

	/**
	 * @return the participiants
	 */
	public List<Employee> getParticipiants()
	{
		return participiants;
	}

	/**
	 * @param participiants the participiants to set
	 */
	public void setParticipiants(List<Employee> participiants)
	{
		this.participiants = participiants;
	}

}
