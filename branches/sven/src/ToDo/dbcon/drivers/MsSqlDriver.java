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
public class MsSqlDriver extends DbDriver
{

	@Override
	public String getEscapedValue(Field columnField, Object dataObject) throws DbStorageException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setObjectValue(Field columnField, Object dataSource, Object dataDestination) throws DbStorageException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
