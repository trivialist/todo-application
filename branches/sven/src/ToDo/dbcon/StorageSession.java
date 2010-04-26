/*
 * $Id$
 */
package todo.dbcon;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author sven
 */
public class StorageSession
{
	public enum STORAGE_STATUS
	{
		UNDEFINED, STORED, LOADED
	};

	public String tableName = "";
	public HashMap<String, Field> columnFields = new HashMap<String, Field>();
	public String generatedColumn = "";
	public Field generatedField = null;
	public int generatedId = -1;
	public Vector<Field> relationFields = new Vector<Field>();
	public STORAGE_STATUS currentStatus = STORAGE_STATUS.UNDEFINED;
}
