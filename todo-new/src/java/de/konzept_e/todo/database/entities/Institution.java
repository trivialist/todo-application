/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.konzept_e.todo.database.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "institution")
public class Institution implements Serializable
{
	@Id
	@Column(name = "institutionId", nullable = false, updatable = false)
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

	public static Institution getDefaultElement(Session dbSession)
	{
		@SuppressWarnings("unchecked")
		List<Institution> institutions = dbSession.createQuery("FROM Institution ORDER BY id ASC").list();
		return institutions.get(0);
	}

	@Override
	public String toString()
	{
		return name;
	}
}
