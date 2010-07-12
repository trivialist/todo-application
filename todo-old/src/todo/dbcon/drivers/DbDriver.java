/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.dbcon.drivers;

import java.lang.reflect.Field;
import todo.dbcon.DbStorageException;

/**
 *
 * @author Sven Skrabal
 */
public abstract class DbDriver
{
	public abstract String getEscapedValue(Field columnField, Object dataObject) throws DbStorageException;
	public abstract void setObjectValue(Field columnField, Object dataSource, Object dataDestination) throws DbStorageException;
}
