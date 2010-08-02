/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.konzept_e.todo.database.entities;

import de.konzept_e.todo.database.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "meetingtype")
public class MeetingType implements Serializable
{
	@Id
	@Column(name = "meetingTypeId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "name")
	private String name;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public static MeetingType getDefaultElement(Session dbSession)
	{
		@SuppressWarnings("unchecked")
		List<MeetingType> meetings = dbSession.createQuery("FROM MeetingType ORDER BY id ASC").list();
		return meetings.get(0);
	}

	public String toString()
	{
		return name;
	}
}
