/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e f�r Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.entity;

public class Topic
{
	private int topicID;
	private String name;
	private String description;

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
