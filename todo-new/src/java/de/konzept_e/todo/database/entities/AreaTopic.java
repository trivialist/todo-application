/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.database.entities;

import java.io.Serializable;
import java.util.List;
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
import org.hibernate.Session;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "area_topic")
public class AreaTopic implements Serializable
{
	@Id
	@Column(name = "areaTopicId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "areaId")
	private Area area;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "topicId")
	private Topic topic;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the area
	 */
	public Area getArea()
	{
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area)
	{
		this.area = area;
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic()
	{
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(Topic topic)
	{
		this.topic = topic;
	}

	public static AreaTopic getDefaultElement(Session dbSession)
	{
		@SuppressWarnings("unchecked")
		List<AreaTopic> areaTopics = dbSession.createQuery("FROM AreaTopic ORDER BY id ASC").list();
		return areaTopics.get(0);
	}
}
