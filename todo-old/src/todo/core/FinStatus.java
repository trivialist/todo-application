/*
 * FinStatus.java
 *
 * Created on 29. Dezember 2006, 16:47
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
public class FinStatus
{
	private String statusName;
	private int statusID;

	/** Creates a new instance of FinStatus */
	public FinStatus(String statusName)
	{
		this.statusName = statusName;
	}

	public FinStatus(int statusID, String statusName)
	{
		this.statusID = statusID;
		this.statusName = statusName;
	}

	public int getStatusID()
	{
		return statusID;
	}

	public String getStatusName()
	{
		return statusName;
	}

	public void setStatusID(int statusID)
	{
		this.statusID = statusID;
	}

	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}
}
