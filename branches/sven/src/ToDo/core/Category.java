/*
 * Category.java
 *
 * Created on 30. Dezember 2006, 20:38
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package todo.core;

import todo.dbcon.DbColumn;
import todo.dbcon.DbId;
import todo.dbcon.DbTable;

/**
 *
 * @author Marcus Hertel
 */
@DbTable(name = "Kategorie")
public class Category
{

	@DbId(name = "KategorieID")
	private int catID;
	@DbColumn(name = "Name")
	private String catName;
	@DbColumn(name = "Beschreibung")
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
