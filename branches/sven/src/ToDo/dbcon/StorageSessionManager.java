/*
 * $Id$
 */
package todo.dbcon;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author sven
 */
public class StorageSessionManager
{
	private HashMap<Integer, StorageSession> storedClasses = new HashMap<Integer, StorageSession>();
	private static StorageSessionManager instance = null;

	private StorageSessionManager()
	{
		//nothing here
	}

	public static synchronized StorageSessionManager getInstance()
	{
		if(instance == null)
		{
			instance = new StorageSessionManager();
		}

		return instance;
	}

	public boolean isObjectRegistered(Object object)
	{
		return storedClasses.containsKey(object.hashCode());
	}

	public StorageSession registerObject(Object object)
	{
		if(storedClasses.containsKey(object.hashCode()))
		{
			return getSession(object);
		}
		
		StorageSession session = new StorageSession();
		storedClasses.put(object.hashCode(), session);

		return session;
	}

	public void deregisterObject(Object object)
	{
		storedClasses.remove(object.hashCode());
	}

	public Set<StorageSession> getRegisteredSessions()
	{
		return (Set<StorageSession>) storedClasses.values();
	}

	public StorageSession getSession(Object object)
	{
		return storedClasses.get(object.hashCode());
	}
}
