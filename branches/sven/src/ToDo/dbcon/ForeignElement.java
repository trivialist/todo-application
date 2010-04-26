/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.dbcon;

import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbRelation;
import todo.dbcon.annotations.DbRelation.RELATION_TYPE;
import todo.dbcon.annotations.DbTable;

/**
 *
 * @author sven
 */

@DbTable(name="ForeignTable")
public class ForeignElement
{
	@DbId(name="ID")
	public int id;
	@DbColumn(name="Name")
	public String name;
	@DbRelation(type = RELATION_TYPE.MANY_TO_ONE)
	public BaseElement base[];
}
