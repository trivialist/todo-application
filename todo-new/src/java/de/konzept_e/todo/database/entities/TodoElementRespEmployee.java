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
@Table(name = "todoelement_resp_employee")
public class TodoElementRespEmployee implements Serializable
{
	@Id
	@Column(name = "todoElementEmployeeId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "todoElementId")
	private TodoElement todoElement;
	
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
	 * @return the todoElement
	 */ public TodoElement getTodoElement()
	{
		return todoElement;
	}

	/**
	 * @param todoElement the todoElement to set
	 */ public void setTodoElement(TodoElement todoElement)
	{
		this.todoElement = todoElement;
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
