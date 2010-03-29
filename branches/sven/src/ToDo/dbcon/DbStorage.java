/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @todo multiple generated fields
 * @todo multiple primary keys
 * @todo relations
 * @todo loadByParam
 * @todo save status in hidden field
 * @todo externalize datatype stuff
 * @todo better load behaviour
 * @todo load with sort criterias
 * @todo use finally to close db connection properly
 * @todo use generics in all methods that are used in select's
 * 
 * @author Sven Skrabal
 */
public class DbStorage
{

	private String tableName = "";
	private HashMap<String, Field> columnFields = new HashMap<String, Field>();
	private String generatedColumn = "";
	private Field generatedField = null;
	private int generatedId = -1;
	private boolean debugEnabled = false;

	public DbStorage()
	{
		DB_ToDo_Connect.openDB();
	}

	private enum DATA_TYPE
	{

		STRING, DATE, BOOLEAN, INTEGER, FLOAT, DOUBLE, LONG, ERROR
	};

	private void findFields(Object objectToScan) throws DbStorageException
	{
		//check for proper input data
		Class<?> objectClass = (Class<?>) objectToScan.getClass();
		if (!objectClass.isAnnotationPresent(DbTable.class))
		{
			throw new DbStorageException("Referenced object hasn't got the right annotation's present.");
		}

		//find table name
		tableName = objectClass.getAnnotation(DbTable.class).name();

		//find columns and keys
		Field[] declaredFields = objectClass.getDeclaredFields();
		for (Field currentField : declaredFields)
		{
			currentField.setAccessible(true);
			if (currentField.isAnnotationPresent(DbColumn.class))
			{
				columnFields.put(currentField.getAnnotation(DbColumn.class).name(), currentField);
			}
			else if (currentField.isAnnotationPresent(DbId.class))
			{
				generatedColumn = currentField.getAnnotation(DbId.class).name();
				generatedField = currentField;
			}
		}
	}

	private DATA_TYPE getDataType(Field fieldToScan)
	{
		DATA_TYPE returnType = DATA_TYPE.ERROR;
		Class<?> variableType = fieldToScan.getType();

		if (variableType.isPrimitive())
		{
			if (variableType.getName().equals("int"))
			{
				returnType = DATA_TYPE.INTEGER;
			}
			else if (variableType.getName().equals("long"))
			{
				returnType = DATA_TYPE.LONG;
			}
			else if (variableType.getName().equals("float"))
			{
				returnType = DATA_TYPE.FLOAT;
			}
			else if (variableType.getName().equals("double"))
			{
				returnType = DATA_TYPE.DOUBLE;
			}
			else if (variableType.getName().equals("boolean"))
			{
				returnType = DATA_TYPE.BOOLEAN;
			}
		}
		else if (variableType.getName().equals(String.class.getName()))
		{
			returnType = DATA_TYPE.STRING;
		}
		else if (variableType.getName().equals(Date.class.getName()))
		{
			returnType = DATA_TYPE.DATE;
		}

		return returnType;
	}

	public void setDebugEnabled(boolean debugEnabled)
	{
		this.debugEnabled = debugEnabled;
	}

	private ResultSet runSqlQuery(String sqlClause) throws DbStorageException
	{
		ResultSet returnResult = null;
		Connection databaseConnection = DB_ToDo_Connect.getCon();

		if (debugEnabled)
		{
			System.out.println(sqlClause);
		}

		try
		{
			Statement databaseStatement = databaseConnection.createStatement();
			returnResult = databaseStatement.executeQuery(sqlClause);

		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to serve your request. Reason:\n" + ex.getMessage());
		}

		return returnResult;
	}

	private ResultSet runSqlStatement(String sqlClause, boolean isInsert) throws DbStorageException
	{
		ResultSet returnResult = null;
		Connection databaseConnection = DB_ToDo_Connect.getCon();

		if (debugEnabled)
		{
			System.out.println(sqlClause);
		}

		try
		{
			Statement databaseStatement = databaseConnection.createStatement();
			databaseConnection.setAutoCommit(false);
			databaseStatement.execute(sqlClause);

			//find generated key
			if (isInsert)
			{
				ResultSet resultSet = databaseStatement.executeQuery("SELECT MAX(" + generatedColumn + ") FROM " + tableName);
				resultSet.next();
				generatedId = resultSet.getInt(1);
			}

			databaseConnection.commit();
			databaseConnection.setAutoCommit(true);

		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to serve your request. Reason:\n" + ex.getMessage());
		}

		return returnResult;
	}

	public void insert(Object objectToInsert) throws DbStorageException
	{
		findFields(objectToInsert);
		String sqlClause = createInsertStatement(objectToInsert);
		runSqlStatement(sqlClause, true);
	}

	public void update(Object objectToUpdate) throws DbStorageException
	{
		findFields(objectToUpdate);
		String sqlClause = createUpdateStatement(objectToUpdate);
		runSqlStatement(sqlClause, false);
	}

	public void delete(Object objectToDelete) throws DbStorageException
	{
		findFields(objectToDelete);
		String sqlClause = createDeleteStatement(objectToDelete);
		runSqlStatement(sqlClause, false);
	}

	public LinkedList<Object> load(Object objectClass, HashMap<String, Object> whereConditions) throws DbStorageException
	{
		findFields(objectClass);

		LinkedList<Object> returnList = new LinkedList<Object>();
		String sqlClause = createSelectStatement(objectClass, whereConditions);

		try
		{
			ResultSet resultSet = runSqlQuery(sqlClause);

			if (resultSet == null)
			{
				return returnList;
			}

			while (resultSet.next())
			{
				try
				{
					Object newObjectInstance = objectClass.getClass().newInstance();

					for (String columnName : columnFields.keySet())
					{
						Field columnField = columnFields.get(columnName);
						setObjectValue(columnField, resultSet.getObject(columnName), newObjectInstance);
					}

					returnList.addLast(newObjectInstance);

				} catch (InstantiationException ex)
				{
					throw new DbStorageException(ex.toString());
				} catch (IllegalAccessException ex)
				{
					throw new DbStorageException(ex.toString());
				}
			}

		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to load the given object from " +
					"the database. Reason:\n" + ex.toString());
		}

		return returnList;
	}

	private String getEscapedValue(Field columnField, Object dataObject) throws DbStorageException
	{
		try
		{
			DATA_TYPE columnType = getDataType(columnField);
			switch (columnType)
			{
				case STRING:
					return "'" + (String) dataObject + "'";

				case DATE:
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					return "'" + sdf.format((Date) dataObject) + "'";

				case BOOLEAN:
					return Boolean.toString((Boolean) dataObject);

				case DOUBLE:
					if (dataObject instanceof java.lang.String)
					{
						return (String) dataObject;
					}
					return Double.toString((Double) dataObject);

				case FLOAT:
					if (dataObject instanceof java.lang.String)
					{
						return (String) dataObject;
					}
					return Float.toString((Float) dataObject);

				case LONG:
					if (dataObject instanceof java.lang.String)
					{
						return (String) dataObject;
					}
					return Long.toString((Long) dataObject);

				case INTEGER:
					if (dataObject instanceof java.lang.String)
					{
						return (String) dataObject;
					}
					return Integer.toString((Integer) dataObject);

				default:
					throw new DbStorageException("Unknown datatype in given input.");
			}
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.getMessage());
		}
	}

	private void setObjectValue(Field columnField, Object dataSource, Object dataDestination) throws DbStorageException
	{
		try
		{
			DATA_TYPE type = getDataType(columnField);
			switch (type)
			{
				case STRING:
					columnField.set(dataDestination, (String) dataSource);
					break;

				case DATE:
					columnField.set(dataDestination, (Date) dataSource);
					break;

				case BOOLEAN:
					columnField.set(dataDestination, (Boolean) dataSource);
					break;

				case DOUBLE:
					columnField.set(dataDestination, (Double) dataSource);
					break;

				case FLOAT:
					columnField.set(dataDestination, (Float) dataSource);
					break;

				case LONG:
					columnField.set(dataDestination, (Long) dataSource);
					break;

				case INTEGER:
					columnField.set(dataDestination, (Integer) dataSource);
					break;

				default:
					throw new DbStorageException("Unknown datatype in given input.");
			}
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.getMessage());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.getMessage());
		}
	}

	private String createInsertStatement(Object objectToInsert) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("INSERT INTO " + tableName + " (");
		StringBuilder dataValues = new StringBuilder();

		try
		{

			Iterator<String> columnIterator = columnFields.keySet().iterator();
			while (columnIterator.hasNext())
			{
				String columnName = columnIterator.next();
				if (columnIterator.hasNext())
				{
					sqlClause.append(columnName + ", ");
					dataValues.append(getEscapedValue(columnFields.get(columnName), columnFields.get(columnName).get(objectToInsert)) + ", ");
				}
				else
				{
					sqlClause.append(columnName);
					dataValues.append(getEscapedValue(columnFields.get(columnName), columnFields.get(columnName).get(objectToInsert)));
				}
			}

			sqlClause.append(") VALUES (");
			sqlClause.append(dataValues);
			sqlClause.append(")");

		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sqlClause.toString();
	}

	private String createUpdateStatement(Object objectToUpdate) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("UPDATE " + tableName + " SET ");
		StringBuilder whereClause = new StringBuilder("");

		try
		{

			//data
			Iterator<String> columnIterator = columnFields.keySet().iterator();
			while (columnIterator.hasNext())
			{
				String columnName = columnIterator.next();
				if (columnIterator.hasNext())
				{
					sqlClause.append(columnName + " = " + getEscapedValue(columnFields.get(columnName), columnFields.get(columnName).get(objectToUpdate)) + ", ");
				}
				else
				{
					sqlClause.append(columnName + " = " + getEscapedValue(columnFields.get(columnName), columnFields.get(columnName).get(objectToUpdate)));
				}
			}
			//where condition
			whereClause.append(generatedColumn + " = " + getEscapedValue(generatedField, generatedField.get(objectToUpdate)));
			sqlClause.append(" WHERE " + whereClause);

		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sqlClause.toString();
	}

	private String createDeleteStatement(Object objectToDelete) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
		try
		{
			//where condition
			sqlClause.append(generatedColumn + " = " + getEscapedValue(generatedField, generatedField.get(objectToDelete)));
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sqlClause.toString();
	}

	private Field findField(Object objectToScan, String columnName) throws DbStorageException
	{
		Field resultField = null;

		//check for proper input data
		Class<?> objectClass = (Class<?>) objectToScan.getClass();
		if (!objectClass.isAnnotationPresent(DbTable.class))
		{
			throw new DbStorageException("Referenced object hasn't got the right annotation's present.");
		}

		//find columns
		Field[] declaredFields = objectClass.getDeclaredFields();
		for (Field currentField : declaredFields)
		{
			currentField.setAccessible(true);
			if ((currentField.isAnnotationPresent(DbColumn.class) && currentField.getAnnotation(DbColumn.class).name().equals(columnName)) || currentField.getName().equals(columnName))
			{
				return currentField;
			}
		}

		if (generatedField.getAnnotation(DbId.class).name().equals(columnName) || generatedField.getName().equals(columnName))
		{
			return generatedField;
		}

		return resultField;
	}

	private String createSelectStatement(Object objectInstance, HashMap<String, Object> loadParameters) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

		Iterator<String> columnIterator = (Iterator<String>) loadParameters.keySet().iterator();
		while (columnIterator.hasNext())
		{
			String columnName = columnIterator.next();
			Field columnField = findField(objectInstance, columnName);

			if (columnField.isAnnotationPresent(DbColumn.class))
			{
				sqlClause.append(columnField.getAnnotation(DbColumn.class).name() + "=" + getEscapedValue(columnField, loadParameters.get(columnName)));
			}
			else
			{
				sqlClause.append(columnField.getAnnotation(DbId.class).name() + "=" + getEscapedValue(columnField, loadParameters.get(columnName)));
			}

			if (columnIterator.hasNext())
			{
				sqlClause.append(" AND ");
			}
		}

		return sqlClause.toString();
	}

	public int getGeneratedKey()
	{
		return generatedId;
	}
}
