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
