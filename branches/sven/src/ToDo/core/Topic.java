/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.core;

import todo.dbcon.DbColumn;
import todo.dbcon.DbId;
import todo.dbcon.DbTable;

/**
 *
 * @author Marcus Hertel
 */
@DbTable(name = "Thema")
public class Topic
{

	@DbId(name = "ThemaID")
	public int topicID;
	@DbColumn(name = "Name")
	public String name;
	@DbColumn(name = "Beschreibung")
	public String description;

	/** Creates a new instance of Topic */
	public Topic(int topicID, String name, String description)
	{
		this.topicID = topicID;
		this.name = name;
		this.description = description;
	}

	public Topic(int topicID)
	{
		this.topicID = topicID;
	}

	public Topic(String name)
	{
		this.name = name;
	}

	public int getTopicID()
	{
		return topicID;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
