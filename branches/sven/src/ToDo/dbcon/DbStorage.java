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
import java.util.Set;

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
 * @author Sven Skrabal
 */
public class DbStorage
{

	private String tableName = "";
	private HashMap<String, Field> fields = new HashMap<String, Field>();
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

	private void findFields(Object e) throws DbStorageException
	{
		//check for proper input data
		Class<?> c = (Class<?>) e.getClass();
		if (!c.isAnnotationPresent(DbEntity.class) || !c.isAnnotationPresent(DbTable.class))
		{
			throw new DbStorageException("Referenced object hasn't got the right annotation's present.");
		}

		//find table name
		tableName = c.getAnnotation(DbTable.class).name();

		//find columns and keys
		Field[] f = c.getDeclaredFields();
		for (Field x : f)
		{
			x.setAccessible(true);
			if (x.isAnnotationPresent(DbColumn.class))
			{
				fields.put(x.getAnnotation(DbColumn.class).name(), x);
			}
			else if (x.isAnnotationPresent(DbId.class))
			{
				generatedColumn = x.getAnnotation(DbId.class).name();
				generatedField = x;
			}
		}
	}

	private DATA_TYPE getDataType(Field f)
	{
		DATA_TYPE type = DATA_TYPE.ERROR;
		Class<?> g = f.getType();

		if (g.isPrimitive())
		{
			if (g.getName().equals("int"))
			{
				type = DATA_TYPE.INTEGER;
			}
			else if (g.getName().equals("long"))
			{
				type = DATA_TYPE.LONG;
			}
			else if (g.getName().equals("float"))
			{
				type = DATA_TYPE.FLOAT;
			}
			else if (g.getName().equals("double"))
			{
				type = DATA_TYPE.DOUBLE;
			}
			else if (g.getName().equals("boolean"))
			{
				type = DATA_TYPE.BOOLEAN;
			}
		}
		else if (g.getName().equals(String.class.getName()))
		{
			type = DATA_TYPE.STRING;
		}
		else if (g.getName().equals(Date.class.getName()))
		{
			type = DATA_TYPE.DATE;
		}

		return type;
	}

	public void setDebugEnabled(boolean debugEnabled)
	{
		this.debugEnabled = debugEnabled;
	}

	private ResultSet runSqlStatement(String sqlClause, boolean isQuery) throws DbStorageException
	{
		ResultSet returnResult = null;
		Connection con = DB_ToDo_Connect.getCon();

		if (debugEnabled)
		{
			System.out.println(sqlClause);
		}

		try
		{
			Statement st = con.createStatement();

			if (isQuery)
			{
				returnResult = st.executeQuery(sqlClause);
			}
			else
			{
				con.setAutoCommit(false);

				st.execute(sqlClause);

				ResultSet rs = st.executeQuery("SELECT MAX(" + generatedColumn + ") FROM " + tableName);
				rs.next();
				generatedId = rs.getInt(1);

				con.commit();
				con.setAutoCommit(true);
			}

		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to serve your request. Reason:\n" + ex.getMessage());
		}

		return returnResult;
	}

	public void insert(Object e) throws DbStorageException
	{
		findFields(e);
		String sql = createInsertStatement(e);
		runSqlStatement(sql, false);
	}

	public void update(Object e) throws DbStorageException
	{
		findFields(e);
		String sql = createUpdateStatement(e);
		runSqlStatement(sql, false);
	}

	public void delete(Object e) throws DbStorageException
	{
		findFields(e);
		String sql = createDeleteStatement(e);
		runSqlStatement(sql, false);
	}

	public Object[] load(Object e, HashMap<String, Object> params) throws DbStorageException
	{
		findFields(e);

		Object[] retObjects = null;
		LinkedList<Object> retList = new LinkedList<Object>();

		String sql = createSelectStatement(e, params);

		try
		{
			ResultSet rst = runSqlStatement(sql, true);

			if (rst == null)
			{
				return retObjects;
			}

			while (rst.next())
			{
				try
				{
					Object x = e.getClass().newInstance();

					for (String col : fields.keySet())
					{
						Field f = fields.get(col);
						setObjectValue(f, rst.getObject(col), x);
					}

					retList.addLast(x);

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
			throw new DbStorageException("The storage engine was unable to load the given object from the database. " +
					"Reason:\n" + ex.toString());
		}

		return retList.toArray(new Object[retList.size()]);
	}

	private String getEscapedValue(Field f, Object e) throws DbStorageException
	{
		try
		{
			DATA_TYPE type = getDataType(f);
			switch (type)
			{
				case STRING:
					return "'" + (String) e + "'";

				case DATE:
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					return "'" + sdf.format((Date) e) + "'";

				case BOOLEAN:
					return Boolean.toString((Boolean) e);

				case DOUBLE:
					if (e instanceof java.lang.String)
					{
						return (String) e;
					}
					return Double.toString((Double) e);

				case FLOAT:
					if (e instanceof java.lang.String)
					{
						return (String) e;
					}
					return Float.toString((Float) e);

				case LONG:
					if (e instanceof java.lang.String)
					{
						return (String) e;
					}
					return Long.toString((Long) e);

				case INTEGER:
					if (e instanceof java.lang.String)
					{
						return (String) e;
					}
					return Integer.toString((Integer) e);

				default:
					throw new DbStorageException("Unknown datatype in given input.");
			}
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.getMessage());
		}
	}

	private void setObjectValue(Field f, Object from, Object to) throws DbStorageException
	{
		try
		{
			DATA_TYPE type = getDataType(f);
			switch (type)
			{
				case STRING:
					f.set(to, (String) from);
					break;

				case DATE:
					break;

				case BOOLEAN:
					break;

				case DOUBLE:
					break;

				case FLOAT:
					break;

				case LONG:
					break;

				case INTEGER:
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

	private String createInsertStatement(Object e) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
		StringBuilder values = new StringBuilder();

		try
		{

			Iterator<String> i = fields.keySet().iterator();
			while (i.hasNext())
			{
				String tmp = i.next();
				if (i.hasNext())
				{
					sql.append(tmp + ", ");
					values.append(getEscapedValue(fields.get(tmp), fields.get(tmp).get(e)) + ", ");
				}
				else
				{
					sql.append(tmp);
					values.append(getEscapedValue(fields.get(tmp), fields.get(tmp).get(e)));
				}
			}

			sql.append(") VALUES (");
			sql.append(values);
			sql.append(")");

		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sql.toString();
	}

	private String createUpdateStatement(Object e) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
		StringBuilder where = new StringBuilder("");

		try
		{

			//data
			Iterator<String> i = fields.keySet().iterator();
			while (i.hasNext())
			{
				String tmp = i.next();
				if (i.hasNext())
				{
					sql.append(tmp + " = " + getEscapedValue(fields.get(tmp), fields.get(tmp).get(e)) + ", ");
				}
				else
				{
					sql.append(tmp + " = " + getEscapedValue(fields.get(tmp), fields.get(tmp).get(e)));
				}
			}
			//where condition
			where.append(generatedColumn + " = " + getEscapedValue(generatedField, generatedField.get(e)));
			sql.append(" WHERE " + where);

		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sql.toString();
	}

	private String createDeleteStatement(Object e) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
		try
		{
			//where condition
			sql.append(generatedColumn + " = " + getEscapedValue(generatedField, generatedField.get(e)));
		} catch (IllegalArgumentException ex)
		{
			throw new DbStorageException(ex.toString());
		} catch (IllegalAccessException ex)
		{
			throw new DbStorageException(ex.toString());
		}

		return sql.toString();
	}

	private Field findField(Object e, String columnName) throws DbStorageException
	{
		Field retField = null;

		//check for proper input data
		Class<?> c = (Class<?>) e.getClass();
		if (!c.isAnnotationPresent(DbEntity.class) || !c.isAnnotationPresent(DbTable.class))
		{
			throw new DbStorageException("Referenced object hasn't got the right annotation's present.");
		}

		//find columns
		Field[] f = c.getDeclaredFields();
		for (Field x : f)
		{
			x.setAccessible(true);
			if ((x.isAnnotationPresent(DbColumn.class) && x.getAnnotation(DbColumn.class).name().equals(columnName)) || x.getName().equals(columnName))
			{
				return x;
			}
		}

		if (generatedField.getAnnotation(DbId.class).name().equals(columnName) || generatedField.getName().equals(columnName))
		{
			return generatedField;
		}

		return retField;
	}

	private String createSelectStatement(Object e, HashMap<String, Object> params) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

		Iterator<String> it = (Iterator<String>) params.keySet().iterator();
		while (it.hasNext())
		{
			String column = it.next();
			Field colName = findField(e, column);

			if (colName.isAnnotationPresent(DbColumn.class))
			{
				sql.append(colName.getAnnotation(DbColumn.class).name() + "=" + getEscapedValue(colName, params.get(column)));
			}
			else
			{
				sql.append(colName.getAnnotation(DbId.class).name() + "=" + getEscapedValue(colName, params.get(column)));
			}

			if (it.hasNext())
			{
				sql.append(" AND ");
			}
		}

		return sql.toString();
	}

	public int getGeneratedKey()
	{
		return generatedId;
	}
}
