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

public class Area
{
	private int areaID;
	private String areaName;
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
