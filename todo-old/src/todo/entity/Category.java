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

public class Category
{
	private int catID;
	private String catName;
	private String catDescription;

	/** Creates a new instance of Category */
	public Category(int catID, String catName, String catDescription)
	{
		this.catID = catID;
		this.catName = catName;
		this.catDescription = catDescription;
	}

	public Category(String catName, String catDescription)
	{
		this.catName = catName;
		this.catDescription = catDescription;
	}

	public int getCatID()
	{
		return catID;
	}

	public String getCatName()
	{
		return catName;
	}

	public String getCatDescription()
	{
		return catDescription;
	}

	public void setCatID(int catID)
	{
		this.catID = catID;
	}

	public void setCatName(String catName)
	{
		this.catName = catName;
	}

	public void setCatDescription(String catDescription)
	{
		this.catDescription = catDescription;
	}
}
