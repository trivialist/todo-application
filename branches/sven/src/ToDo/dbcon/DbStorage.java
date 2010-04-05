/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import todo.dbcon.annotations.DbId;
import todo.dbcon.annotations.DbColumn;
import todo.dbcon.annotations.DbTable;
import todo.dbcon.drivers.DbDriver;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import todo.dbcon.annotations.DbRelation;

/**
 * @todo multiple generated fields
 * @todo multiple primary keys
 * @todo save status in hidden field: changed/unchanged fields
 * @todo better load behaviour
 * @todo load with sort criterias
 * 
 * @author Sven Skrabal
 */
public class DbStorage
{

	protected class StorageSession
	{

		public String tableName = "";
		public HashMap<String, Field> columnFields = new HashMap<String, Field>();
		public String generatedColumn = "";
		public Field generatedField = null;
		public int generatedId = -1;
	}
	private boolean debugEnabled = false;
	private DbDriver currentDriver = null;
	private Connection databaseConnection = null;

	public void setDbDriver(DbDriver dbDriver)
	{
		currentDriver = dbDriver;
	}

	public void setDatabaseConnection(Connection newConnection)
	{
		databaseConnection = newConnection;
	}

	private void findFields(Object objectToScan, StorageSession session) throws DbStorageException
	{
		//check for proper input data
		Class<?> objectClass = (Class<?>) objectToScan.getClass();
		if (!objectClass.isAnnotationPresent(DbTable.class))
		{
			throw new DbStorageException("Referenced object hasn't got the right annotation's present.");
		}

		//find table name
		session.tableName = objectClass.getAnnotation(DbTable.class).name();

		//find columns and keys
		Field[] declaredFields = objectClass.getDeclaredFields();
		for (Field currentField : declaredFields)
		{
			currentField.setAccessible(true);
			if (currentField.isAnnotationPresent(DbColumn.class))
			{
				session.columnFields.put(currentField.getAnnotation(DbColumn.class).name(), currentField);
			}
			else if (currentField.isAnnotationPresent(DbId.class))
			{
				session.generatedColumn = currentField.getAnnotation(DbId.class).name();
				session.generatedField = currentField;
			}
		}
	}

	public void setDebugEnabled(boolean debugEnabled)
	{
		this.debugEnabled = debugEnabled;
	}

	private ResultSet runSqlQuery(String sqlClause) throws DbStorageException
	{
		ResultSet returnResult = null;

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

	private ResultSet runSqlStatement(String sqlClause, boolean isInsert, StorageSession session) throws DbStorageException
	{
		ResultSet returnResult = null;

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
				ResultSet resultSet = databaseStatement.executeQuery("SELECT MAX(" + session.generatedColumn + ") FROM " + session.tableName);
				resultSet.next();
				session.generatedId = resultSet.getInt(1);
			}

			databaseConnection.commit();
			databaseConnection.setAutoCommit(true);

		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to serve your request. Reason:\n" + ex.getMessage());
		}

		return returnResult;
	}

	public StorageSession insert(Object objectToInsert) throws DbStorageException
	{
		StorageSession session = new StorageSession();
		findFields(objectToInsert, session);
		String sqlClause = createInsertStatement(objectToInsert, session);
		runSqlStatement(sqlClause, true, session);

		return session;
	}

	public StorageSession update(Object objectToUpdate) throws DbStorageException
	{
		StorageSession session = new StorageSession();
		findFields(objectToUpdate, session);
		String sqlClause = createUpdateStatement(objectToUpdate, session);
		runSqlStatement(sqlClause, false, session);

		return session;
	}

	public void delete(Object objectToDelete) throws DbStorageException
	{
		StorageSession session = new StorageSession();
		findFields(objectToDelete, session);
		String sqlClause = createDeleteStatement(objectToDelete, session);
		runSqlStatement(sqlClause, false, session);
	}

	public LinkedList<Object> load(Object objectClass, HashMap<String, Object> whereConditions) throws DbStorageException
	{
		StorageSession session = new StorageSession();
		findFields(objectClass, session);

		LinkedList<Object> returnList = new LinkedList<Object>();
		String sqlClause = createSelectStatement(objectClass, whereConditions, session);

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

					for (String columnName : session.columnFields.keySet())
					{
						Field columnField = session.columnFields.get(columnName);
						currentDriver.setObjectValue(columnField, resultSet.getObject(columnName), newObjectInstance);
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

	private String createInsertStatement(Object objectToInsert, StorageSession session) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("INSERT INTO " + session.tableName + " (");
		StringBuilder dataValues = new StringBuilder();

		try
		{

			Iterator<String> columnIterator = session.columnFields.keySet().iterator();
			while (columnIterator.hasNext())
			{
				String columnName = columnIterator.next();
				Field columnField = session.columnFields.get(columnName);
				Object storeValue = session.columnFields.get(columnName).get(objectToInsert);

				if (columnField.isAnnotationPresent(DbRelation.class))
				{
					System.out.println("Oh, jeah!");
					StorageSession result = insert(storeValue);
					storeValue = result.generatedId;
					columnField = result.generatedField;
					System.out.println(result.generatedId);
				}

				if (columnIterator.hasNext())
				{
					sqlClause.append(columnName + ", ");
					dataValues.append(currentDriver.getEscapedValue(columnField, storeValue) + ", ");
				}
				else
				{
					sqlClause.append(columnName);
					dataValues.append(currentDriver.getEscapedValue(columnField, storeValue));
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

	private String createUpdateStatement(Object objectToUpdate, StorageSession session) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("UPDATE " + session.tableName + " SET ");
		StringBuilder whereClause = new StringBuilder("");

		try
		{

			//data
			Iterator<String> columnIterator = session.columnFields.keySet().iterator();
			while (columnIterator.hasNext())
			{
				String columnName = columnIterator.next();
				if (columnIterator.hasNext())
				{
					sqlClause.append(columnName + " = " + currentDriver.getEscapedValue(session.columnFields.get(columnName), session.columnFields.get(columnName).get(objectToUpdate)) + ", ");
				}
				else
				{
					sqlClause.append(columnName + " = " + currentDriver.getEscapedValue(session.columnFields.get(columnName), session.columnFields.get(columnName).get(objectToUpdate)));
				}
			}
			//where condition
			whereClause.append(session.generatedColumn + " = " + currentDriver.getEscapedValue(session.generatedField, session.generatedField.get(objectToUpdate)));
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

	private String createDeleteStatement(Object objectToDelete, StorageSession session) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("DELETE FROM " + session.tableName + " WHERE ");
		try
		{
			//where condition
			sqlClause.append(session.generatedColumn + " = " + currentDriver.getEscapedValue(session.generatedField, session.generatedField.get(objectToDelete)));
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sqlClause.toString();
	}

	private Field findField(Object objectToScan, String columnName, StorageSession session) throws DbStorageException
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

		if (session.generatedField.getAnnotation(DbId.class).name().equals(columnName) || session.generatedField.getName().equals(columnName))
		{
			return session.generatedField;
		}

		return resultField;
	}

	private String createSelectStatement(Object objectInstance, HashMap<String, Object> loadParameters, StorageSession session) throws DbStorageException
	{
		StringBuilder sqlClause = new StringBuilder("SELECT * FROM " + session.tableName + " WHERE ");

		Iterator<String> columnIterator = (Iterator<String>) loadParameters.keySet().iterator();
		while (columnIterator.hasNext())
		{
			String columnName = columnIterator.next();
			Field columnField = findField(objectInstance, columnName, session);

			if (columnField.isAnnotationPresent(DbColumn.class))
			{
				sqlClause.append(columnField.getAnnotation(DbColumn.class).name() + "=" + currentDriver.getEscapedValue(columnField, loadParameters.get(columnName)));
			}
			else
			{
				sqlClause.append(columnField.getAnnotation(DbId.class).name() + "=" + currentDriver.getEscapedValue(columnField, loadParameters.get(columnName)));
			}

			if (columnIterator.hasNext())
			{
				sqlClause.append(" AND ");
			}
		}

		return sqlClause.toString();
	}

	/*public int getGeneratedKey()
	{
	return generatedId;
	}*/
}
