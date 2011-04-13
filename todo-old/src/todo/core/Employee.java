/*
 * Employee.java
 *
 * Created on 10. Januar 2007, 01:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package todo.core;

/**
 *
 * @author Marcus Hertel
 */
public class Employee
{
	private int employeeID;
	private String name;
	private String lastName;

	/** Creates a new instance of Employee */
	public Employee()
	{
	}

	public Employee(int employeeID, String name, String lastName)
	{
		this.employeeID = employeeID;
		this.name = name;
		this.lastName = lastName;
	}

	public int getEmployeeID()
	{
		return employeeID;
	}

	public String getName()
	{
		return name;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setEmployeeID(int employeeID)
	{
		this.employeeID = employeeID;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
}
