/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.konzept_e.todo.database.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "memo")
public class Memo implements Serializable
{
	@Id
	@Column(name = "memoId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "content")
	private String content;

	@Column(name = "created")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date created;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "todoElementId")
	private TodoElement todoElement;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "employeeId")
	private Employee employee;

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
	 * @return the created
	 */
	public Date getCreated()
	{
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created)
	{
		this.created = created;
	}

	/**
	 * @return the todoElement
	 */
	public TodoElement getTodoElement()
	{
		return todoElement;
	}

	/**
	 * @param todoElement the todoElement to set
	 */
	public void setTodoElement(TodoElement todoElement)
	{
		this.todoElement = todoElement;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee()
	{
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee)
	{
		this.employee = employee;
	}
}
