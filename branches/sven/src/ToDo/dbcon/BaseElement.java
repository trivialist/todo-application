/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import todo.dbcon.annotations.DbRelation;
import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbRelation.RELATION_TYPE;
import todo.dbcon.annotations.DbTable;

/**
 *
 * @author sven
 */
@DbTable(name = "BaseTable")
public class BaseElement
{

	@DbId(name = "ID")
	public int id;
	@DbColumn(name = "Name")
	public String name;
	@DbColumn(name = "ForeignID")
	@DbRelation(type = RELATION_TYPE.ONE_TO_MANY)
	public ForeignElement element;
}
