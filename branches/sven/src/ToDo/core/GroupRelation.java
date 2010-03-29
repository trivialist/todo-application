/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ToDo.core;

import todo.dbcon.DbColumn;
import todo.dbcon.DbId;
import todo.dbcon.DbTable;

/**
 * @author Sven Skrabal
 */
@DbTable(name="TPG")
public class GroupRelation
{
	@DbColumn(name = "ToDoID")
	private int toDoID = 0;
	@DbColumn(name = "GruppenID")
	private int groupID = 0;
	@DbColumn(name = "PersonID")
	private int personID = 0;
	@DbId(name = "TPG_ID")
	private int tpgID = 0;

	public int getToDoID()
	{
		return toDoID;
	}

	public void setToDoID(int toDoID)
	{
		this.toDoID = toDoID;
	}

	public int getGroupID()
	{
		return groupID;
	}

	public void setGroupID(int groupID)
	{
		this.groupID = groupID;
	}

	public int getPersonID()
	{
		return personID;
	}

	public void setPersonID(int personID)
	{
		this.personID = personID;
	}

	public int getTpgID()
	{
		return tpgID;
	}

	public void setTpgID(int tpgID)
	{
		this.tpgID = tpgID;
	}
}
