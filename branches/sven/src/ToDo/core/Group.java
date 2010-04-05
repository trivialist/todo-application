/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.core;

import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbTable;

/**
 * @author Sven Skrabal
 */
@DbTable(name = "Gruppen")
public class Group
{

	@DbColumn(name = "Name")
	private String name = "";
	@DbColumn(name = "Beschreibung")
	private String description = "";
	@DbId(name = "GruppenID")
	private int id = 0;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
