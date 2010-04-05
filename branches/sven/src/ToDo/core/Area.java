/*
 * Area.java
 *
 * Created on 23. Juli 2007, 14:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package todo.core;

import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbTable;

/**
 *
 * @author Marcus Hertel
 */
@DbTable(name = "Bereich")
public class Area
{

	@DbId(name = "BereichID")
	private int areaID;
	@DbColumn(name = "Name")
	private String areaName;
	@DbColumn(name = "Beschreibung")
	private String areaDescription;

	/** Creates a new instance of Area */
	public Area(int areaID, String areaName, String areaDescription)
	{
		this.areaID = areaID;
		this.areaName = areaName;
		this.areaDescription = areaDescription;
	}

	public Area(String areaName, String areaDescription)
	{
		this.areaName = areaName;
		this.areaDescription = areaDescription;
	}

	public Area(int areaID)
	{
		this.areaID = areaID;
	}

	public int getAreaID()
	{
		return areaID;
	}

	public String getAreaName()
	{
		return areaName;
	}

	public String getAreaDescription()
	{
		return areaDescription;
	}

	public void setAreaID(int areaID)
	{
		this.areaID = areaID;
	}

	public void setAreaName(String areaName)
	{
		this.areaName = areaName;
	}

	public void setAreaDescription(String areaDescription)
	{
		this.areaDescription = areaDescription;
	}
}
