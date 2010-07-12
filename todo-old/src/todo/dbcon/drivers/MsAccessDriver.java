/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon.drivers;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import todo.dbcon.DbStorageException;

/**
 *
 * @author Sven Skrabal
 */
public class MsAccessDriver extends DbDriver
{
	@Override
	public String getEscapedValue(Field columnField, Object dataObject) throws DbStorageException
	{
		Class<?> variableType = columnField.getType();

		if (variableType.isPrimitive())
		{
			if (variableType.getName().equals("int"))
			{
				if (dataObject instanceof java.lang.String)
				{
					return (String) dataObject;
				}
				return Integer.toString((Integer) dataObject);
			}
			else if (variableType.getName().equals("long"))
			{
				if (dataObject instanceof java.lang.String)
				{
					return (String) dataObject;
				}
				return Long.toString((Long) dataObject);
			}
			else if (variableType.getName().equals("float"))
			{
				if (dataObject instanceof java.lang.String)
				{
					return (String) dataObject;
				}
				return Float.toString((Float) dataObject);
			}
			else if (variableType.getName().equals("double"))
			{
				if (dataObject instanceof java.lang.String)
				{
					return (String) dataObject;
				}
				return Double.toString((Double) dataObject);
			}
			else if (variableType.getName().equals("boolean"))
			{
				return Boolean.toString((Boolean) dataObject);
			}
		}
		else if (variableType.getName().equals(String.class.getName()))
		{
			return "'" + (String) dataObject + "'";
		}
		else if (variableType.getName().equals(Date.class.getName()))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return "'" + sdf.format((Date) dataObject) + "'";
		}

		throw new DbStorageException("The MsAccessDriver was unable to escape the given value(s).");
	}

	@Override
	public void setObjectValue(Field columnField, Object dataSource, Object dataDestination) throws DbStorageException
	{
		Class<?> variableType = columnField.getType();

		try
		{
			if (variableType.isPrimitive())
			{
				if (variableType.getName().equals("int"))
				{

					columnField.set(dataDestination, (Integer) dataSource);
				}
				else if (variableType.getName().equals("long"))
				{
					columnField.set(dataDestination, (Long) dataSource);
				}
				else if (variableType.getName().equals("float"))
				{
					columnField.set(dataDestination, (Float) dataSource);
				}
				else if (variableType.getName().equals("double"))
				{
					columnField.set(dataDestination, (Double) dataSource);
				}
				else if (variableType.getName().equals("boolean"))
				{
					columnField.set(dataDestination, (Boolean) dataSource);
				}
			}
			else if (variableType.getName().equals(String.class.getName()))
			{
				columnField.set(dataDestination, (String) dataSource);
			}
			else if (variableType.getName().equals(Date.class.getName()))
			{
				columnField.set(dataDestination, (Date) dataSource);
			}
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException("The MsAccessDriver was unable to set the given value(s) into the destination object.");
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException("The MsAccessDriver was unable to set the given value(s) into the destination object.");
		}

		//throw new DbStorageException("The MsAccessDriver was unable to set the given value(s) into the destination object.");
	}
}
