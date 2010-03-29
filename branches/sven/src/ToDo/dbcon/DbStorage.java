/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @todo multiple generated fields
 * @todo multiple primary keys
 * @todo relations
 * @todo loadByParam
 * @todo save status in hidden field
 * @todo externalize datatype stuff
 * @todo better load behaviour
 * @author sven
 */
public class DbStorage
{

	String tableName = "";
	HashMap<String, Field> fields = new HashMap<String, Field>();
	String generatedColumn = "";
	Field generatedField = null;

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

	public void insert(Object e) throws DbStorageException
	{
		findFields(e);

		String sql = createInsertStatement(e);

		DB_ToDo_Connect.openDB();
		Connection con = DB_ToDo_Connect.getCon();

		try
		{
			Statement st = con.createStatement();
			//st.execute(sql);
			System.out.println(sql);
		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to create a new object in the database.");
		}

		DB_ToDo_Connect.closeDB(con);
	}

	public void update(Object e) throws DbStorageException
	{
		findFields(e);

		String sql = createUpdateStatement(e);

		DB_ToDo_Connect.openDB();
		Connection con = DB_ToDo_Connect.getCon();

		try
		{
			Statement st = con.createStatement();
			//st.execute(sql);
			System.out.println(sql);
		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to update the given object in the database.");
		}

		DB_ToDo_Connect.closeDB(con);
	}

	public void delete(Object e) throws DbStorageException
	{
		findFields(e);

		String sql = createDeleteStatement(e);

		DB_ToDo_Connect.openDB();
		Connection con = DB_ToDo_Connect.getCon();

		try
		{
			Statement st = con.createStatement();
			//st.execute(sql);
			System.out.println(sql);
		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to delete the given object in the database.");
		}

		DB_ToDo_Connect.closeDB(con);
	}

	public void load(Object e, HashMap<String, Object> params) throws DbStorageException
	{
		findFields(e);

		String sql = createSelectStatement(e, params);

		DB_ToDo_Connect.openDB();
		Connection con = DB_ToDo_Connect.getCon();
		try
		{
			Statement st = con.createStatement();
			//st.execute(sql);
			System.out.println(sql);
		} catch (SQLException ex)
		{
			throw new DbStorageException("The storage engine was unable to load the given object from the database.");
		}

		DB_ToDo_Connect.closeDB(con);
	}

	private String getEscapedValue(Field f, Object e) throws DbStorageException
	{
		try
		{
			DATA_TYPE type = getDataType(f);
			switch (type)
			{
				case STRING:
					return "'" + (String) f.get(e) + "'";

				case DATE:
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd h:m:s");
					return "#" + sdf.format((Date) f.get(e)) + "#";

				case BOOLEAN:
					return Boolean.toString((Boolean) f.get(e));

				case DOUBLE:
					return Double.toString((Double) f.get(e));

				case FLOAT:
					return Float.toString((Float) f.get(e));

				case LONG:
					return Long.toString((Long) f.get(e));

				case INTEGER:
					return Integer.toString((Integer) f.get(e));

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

		Iterator<String> i = fields.keySet().iterator();
		while (i.hasNext())
		{
			String tmp = i.next();
			if (i.hasNext())
			{
				sql.append(tmp + ", ");
				values.append(getEscapedValue(fields.get(tmp), e) + ", ");
			}
			else
			{
				sql.append(tmp);
				values.append(getEscapedValue(fields.get(tmp), e));
			}
		}

		sql.append(") VALUES (");
		sql.append(values);
		sql.append(")");

		return sql.toString();
	}

	private String createUpdateStatement(Object e) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
		StringBuilder where = new StringBuilder("");

		//data
		Iterator<String> i = fields.keySet().iterator();
		while (i.hasNext())
		{
			String tmp = i.next();
			if (i.hasNext())
			{
				sql.append(tmp + " = " + getEscapedValue(fields.get(tmp), e) + ", ");
			}
			else
			{
				sql.append(tmp + " = " + getEscapedValue(fields.get(tmp), e));
			}
		}

		//where condition
		where.append(generatedColumn + " = " + getEscapedValue(generatedField, e));
		sql.append(" WHERE " + where);

		return sql.toString();
	}

	private String createDeleteStatement(Object e) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");

		//where condition
		sql.append(generatedColumn + " = " + getEscapedValue(generatedField, e));

		return sql.toString();
	}

	private String createSelectStatement(Object e, HashMap<String, Object> params) throws DbStorageException
	{
		StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

		//where condition
		sql.append(generatedColumn + " = " + getEscapedValue(generatedField, e));

		throw new DbStorageException("not implemented right now!");

		//return sql.toString();
	}
}
