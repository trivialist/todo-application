/*
 * $Id$
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
import todo.dbcon.StorageSession.STORAGE_STATUS;
import todo.dbcon.annotations.DbRelation;

/**
 * Main class for persistence
 *
 * @author Sven Skrabal
 *
 * @todo multiple generated fields
 * @todo multiple primary keys
 * @todo save status in hidden field: changed/unchanged fields
 * @todo better load behaviour
 * @todo load with sort criterias
 * @todo DbStorage/DB must be a singleton
 * @todo switch to prepared statements
 * @todo add relations
 * @todo support collection/arrays
 * @todo use generics
 * @todo close db connections properly
 *
 */
public class DbStorage
{
	private boolean debugEnabled = false;
	private DbDriver currentDriver = null;
	private Connection databaseConnection = null;
	private StorageSessionManager sessionManager = StorageSessionManager.getInstance();

	public void setDbDriver(DbDriver dbDriver)
	{
		currentDriver = dbDriver;
	}

	public void setDatabaseConnection(Connection newConnection)
	{
		databaseConnection = newConnection;
	}

	private void findFields(Class<?> objectClass, StorageSession session) throws DbStorageException
	{
		//check for proper input data
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
			else if (currentField.isAnnotationPresent(DbRelation.class))
			{
				session.relationFields.add(currentField);
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

		Statement databaseStatement = null;
		try
		{
			databaseStatement = databaseConnection.createStatement();
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

		Statement databaseStatement = null;
		try
		{
			databaseStatement = databaseConnection.createStatement();
			databaseConnection.setAutoCommit(false);
			databaseStatement.execute(sqlClause);

			//find generated key
			if (isInsert)
			{
				ResultSet resultSet = databaseStatement.executeQuery("SELECT MAX(" + session.generatedColumn + ") FROM " + session.tableName);
				resultSet.next();
				session.generatedId = resultSet.getInt(1);
				resultSet.close();
			}

			databaseConnection.commit();
			databaseConnection.setAutoCommit(true);

		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to serve your request. Reason:\n" + ex.getMessage());
		}
		finally
		{
			try
			{
				if (databaseStatement != null)
				{
					databaseStatement.close();
				}
			} catch (SQLException ex)
			{
				throw new DbStorageException("The storage engine was unable to serve your request. Reason:\n" + ex.getMessage());
			}
		}

		return returnResult;
	}

	public StorageSession insert(Object objectToInsert) throws DbStorageException
	{
		StorageSession session = sessionManager.registerObject(objectToInsert);

		if(session.currentStatus == STORAGE_STATUS.UNDEFINED)
		{
			findFields(objectToInsert.getClass(), session);
			String sqlClause = createInsertStatement(objectToInsert, session);
			runSqlStatement(sqlClause, true, session);

			session.currentStatus = STORAGE_STATUS.STORED;
		}

		return session;
	}

	public StorageSession update(Object objectToUpdate) throws DbStorageException
	{
		StorageSession session = sessionManager.registerObject(objectToUpdate);

		if(session.currentStatus != STORAGE_STATUS.LOADED || session.currentStatus != STORAGE_STATUS.STORED)
		{
			throw new DbStorageException("Object cannot be updated. It either wasnt loaded from the database nor it was saved before.");
		}

		findFields(objectToUpdate.getClass(), session);
		String sqlClause = createUpdateStatement(objectToUpdate, session);
		runSqlStatement(sqlClause, false, session);
		session.currentStatus = STORAGE_STATUS.STORED;

		return session;
	}

	public void delete(Object objectToDelete) throws DbStorageException
	{
		StorageSession session = sessionManager.registerObject(objectToDelete);

		if(session.currentStatus != STORAGE_STATUS.LOADED || session.currentStatus != STORAGE_STATUS.STORED)
		{
			throw new DbStorageException("Object cannot be deleted. It either wasnt loaded from the database nor it was saved before.");
		}

		findFields(objectToDelete.getClass(), session);
		String sqlClause = createDeleteStatement(objectToDelete, session);
		runSqlStatement(sqlClause, false, session);
		session.currentStatus = STORAGE_STATUS.STORED;
	}

	public Object loadFirst(Object objectClass, HashMap<String, Object> whereConditions) throws DbStorageException
	{
		return load(objectClass, whereConditions).get(0);
	}

	public LinkedList<Object> load(Object objectClass, HashMap<String, Object> whereConditions) throws DbStorageException
	{
		Object newObjectInstance = null;
		try
		{
			newObjectInstance = objectClass.getClass().newInstance();
		} catch (InstantiationException ex)
		{
			throw new DbStorageException(ex.getMessage());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.getMessage());
		}

		if(newObjectInstance == null)
		{
			throw new DbStorageException("Null-pointer found.");
		}

		StorageSession session = sessionManager.registerObject(newObjectInstance);
		findFields(objectClass.getClass(), session);

		LinkedList<Object> returnList = new LinkedList<Object>();
		String sqlClause = createSelectStatement(objectClass, whereConditions, session);

		try
		{
			ResultSet resultSet = runSqlQuery(sqlClause);
			session.currentStatus = STORAGE_STATUS.LOADED;

			if (resultSet == null)
			{
				return returnList;
			}

			while (resultSet.next())
			{
				for (String columnName : session.columnFields.keySet())
				{
					Field columnField = session.columnFields.get(columnName);
					currentDriver.setObjectValue(columnField, resultSet.getObject(columnName), newObjectInstance);
				}
				
				session.generatedId = resultSet.getInt(session.generatedColumn);

				returnList.addLast(newObjectInstance);
			}

		} catch (Exception ex)
		{
			throw new DbStorageException("The storage engine was unable to load the given object from " +
					"the database. Reason:\n" + ex.toString());
		}

		return returnList;
	}

	private String createInsertStatement(Object objectToInsert, StorageSession session) throws DbStorageException
	{
		if (objectToInsert == null)
		{
			throw new DbStorageException("Null-pointer found.");
		}

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

				if (storeValue == null)
				{
					throw new DbStorageException("Null-pointer found.");
				}

				if (columnField.isAnnotationPresent(DbRelation.class))
				{
					StorageSession result = insert(storeValue);
					storeValue = result.generatedId;
					columnField = result.generatedField;
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
		if (objectToUpdate == null)
		{
			throw new DbStorageException("Null-pointer found.");
		}

		StringBuilder sqlClause = new StringBuilder("UPDATE " + session.tableName + " SET ");
		StringBuilder whereClause = new StringBuilder("");

		try
		{

			//data
			Iterator<String> columnIterator = session.columnFields.keySet().iterator();
			while (columnIterator.hasNext())
			{
				String columnName = columnIterator.next();
				Field columField = session.columnFields.get(columnName);
				Object storeValue = session.columnFields.get(columnName).get(objectToUpdate);

				sqlClause.append(columnName + " = " + currentDriver.getEscapedValue(columField, storeValue));

				if (columnIterator.hasNext())
				{
					sqlClause.append(", ");
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
		if (objectToDelete == null)
		{
			throw new DbStorageException("Null-pointer found.");
		}

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

	private Field findField(Class<?> objectClass, String columnName, StorageSession session) throws DbStorageException
	{
		//check for proper input data
		if (!objectClass.isAnnotationPresent(DbTable.class))
		{
			throw new DbStorageException("Referenced object hasn't got the right annotation's present.");
		}

		//find columns and check their name
		Field[] declaredFields = objectClass.getDeclaredFields();
		for (Field currentField : declaredFields)
		{
			currentField.setAccessible(true);
			if ((currentField.isAnnotationPresent(DbColumn.class) && currentField.getAnnotation(DbColumn.class).name().equals(columnName)) ||
					currentField.getName().equals(columnName))
			{
				return currentField;
			}
		}

		//check if generated field matches search criteria
		if (session.generatedField.getAnnotation(DbId.class).name().equals(columnName) || session.generatedField.getName().equals(columnName))
		{
			return session.generatedField;
		}

		//check if relation field matches search criteria

		throw new DbStorageException("Given column name wasnt found.");
	}

	private String createSelectStatement(Object objectInstance, HashMap<String, Object> loadParameters, StorageSession session) throws DbStorageException
	{
		if (objectInstance == null)
		{
			throw new DbStorageException("Null-pointer found.");
		}

		StringBuilder sqlClause = new StringBuilder("SELECT * FROM " + session.tableName + " WHERE ");

		Iterator<String> columnIterator = loadParameters.keySet().iterator();
		while (columnIterator.hasNext())
		{
			String columnName = columnIterator.next();
			Field columnField = findField(objectInstance.getClass(), columnName, session);

			if (loadParameters.get(columnName) == null)
			{
				throw new DbStorageException("Null-pointer found.");
			}

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
}
