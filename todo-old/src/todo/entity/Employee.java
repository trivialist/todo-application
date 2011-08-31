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
