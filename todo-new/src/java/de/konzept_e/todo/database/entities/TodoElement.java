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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "todoelement")
public class TodoElement implements Serializable
{
	@Id
	@Column(name = "todoElementId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "content")
	private String content;

	@Column(name = "reminderDate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date reminderDate;

	@Column(name = "reminderEnabled")
	private boolean reminderEnabled;

	@Column(name = "heading")
	private String heading;

	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "copyReason")
	private String copyReason;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "categoryId")
	private Category category;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "meetingId")
	private Meeting meeting;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "statusId")
	private Status status;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "institutionId")
	private Institution institution;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "areaId")
	private Area area;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "copyOfTodoElementId", nullable=true)
	private TodoElement copyOf;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "reminderMeetingTypeId")
	private MeetingType reminderType;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable( name="todoelement_resp_employee",
				joinColumns=@JoinColumn(name="todoElementId"),
				inverseJoinColumns=@JoinColumn(name="employeeId"))
	private List<Employee> responsibles;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable( name="todoelement_inv_employee",
				joinColumns=@JoinColumn(name="todoElementId"),
				inverseJoinColumns=@JoinColumn(name="employeeId"))
	private List<Employee> involved;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "areaTopicId", nullable=true)
	private AreaTopic areaTopic;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return the reminderDate
	 */
	public Date getReminderDate()
	{
		return reminderDate;
	}

	/**
	 * @param reminderDate the reminderDate to set
	 */
	public void setReminderDate(Date reminderDate)
	{
		this.reminderDate = reminderDate;
	}

	/**
	 * @return the remindertEnabled
	 */
	public boolean isReminderEnabled()
	{
		return reminderEnabled;
	}

	/**
	 * @param remindertEnabled the remindertEnabled to set
	 */
	public void setReminderEnabled(boolean remindertEnabled)
	{
		this.reminderEnabled = remindertEnabled;
	}

	/**
	 * @return the heading
	 */
	public String getHeading()
	{
		return heading;
	}

	/**
	 * @param heading the heading to set
	 */
	public void setHeading(String heading)
	{
		this.heading = heading;
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
	 * @return the copyReason
	 */
	public String getCopyReason()
	{
		return copyReason;
	}

	/**
	 * @param copyReason the copyReason to set
	 */
	public void setCopyReason(String copyReason)
	{
		this.copyReason = copyReason;
	}

	/**
	 * @return the category
	 */
	public Category getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category)
	{
		this.category = category;
	}

	/**
	 * @return the meeting
	 */
	public Meeting getMeeting()
	{
		return meeting;
	}

	/**
	 * @param meeting the meeting to set
	 */
	public void setMeeting(Meeting meeting)
	{
		this.meeting = meeting;
	}

	/**
	 * @return the status
	 */
	public Status getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status)
	{
		this.status = status;
	}

	/**
	 * @return the institution
	 */
	public Institution getInstitution()
	{
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(Institution institution)
	{
		this.institution = institution;
	}

	/**
	 * @return the area
	 */
	public Area getArea()
	{
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area)
	{
		this.area = area;
	}

	/**
	 * @return the copyOf
	 */
	public TodoElement getCopyOf()
	{
		return copyOf;
	}

	/**
	 * @param copyOf the copyOf to set
	 */
	public void setCopyOf(TodoElement copyOf)
	{
		this.copyOf = copyOf;
	}

	/**
	 * @return the reminderType
	 */
	public MeetingType getReminderType()
	{
		return reminderType;
	}

	/**
	 * @param reminderType the reminderType to set
	 */
	public void setReminderType(MeetingType reminderType)
	{
		this.reminderType = reminderType;
	}

	/**
	 * @return the responsibles
	 */
	public List<Employee> getResponsibles()
	{
		return responsibles;
	}

	/**
	 * @param responsibles the responsibles to set
	 */
	public void setResponsibles(List<Employee> responsibles)
	{
		this.responsibles = responsibles;
	}

	/**
	 * @return the involved
	 */
	public List<Employee> getInvolved()
	{
		return involved;
	}

	/**
	 * @param involved the involved to set
	 */
	public void setInvolved(List<Employee> involved)
	{
		this.involved = involved;
	}

	/**
	 * @return the areaTopic
	 */ public AreaTopic getAreaTopic()
	{
		return areaTopic;
	}

	/**
	 * @param areaTopic the areaTopic to set
	 */ public void setAreaTopic(AreaTopic areaTopic)
	{
		this.areaTopic = areaTopic;
	}
}
