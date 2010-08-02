/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.database.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "meeting_employee")
public class MeetingEmployee implements Serializable
{
	@Id
	@Column(name = "meetingEmployeeId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "meetingId")
	private Meeting meeting;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "employeeId")
	private Employee employee;

	/**
	 * @return the id
	 */ public int getId()
	{
		return id;
	}

	/**
	 * @return the meeting
	 */ public Meeting getMeeting()
	{
		return meeting;
	}

	/**
	 * @param meeting the meeting to set
	 */ public void setMeeting(Meeting meeting)
	{
		this.meeting = meeting;
	}

	/**
	 * @return the employee
	 */ public Employee getEmployee()
	{
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */ public void setEmployee(Employee employee)
	{
		this.employee = employee;
	}
}
