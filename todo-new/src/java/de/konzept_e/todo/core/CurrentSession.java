/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sven
 */
public class CurrentSession
{
	private final TodoApplication appInstance;
	private final HashMap<Class<? extends ChangeNotifier>, ArrayList<ChangeNotifier>> globalNotifier = new HashMap<Class<? extends ChangeNotifier>, ArrayList<ChangeNotifier>>();

	public CurrentSession(TodoApplication appInstance)
	{
		this.appInstance = appInstance;
	}

	public TodoApplication getAppInstance()
	{
		return appInstance;
	}

	public void registerChangeNotifier(Class<? extends ChangeNotifier> notifyAction, ChangeNotifier notifyObject)
	{
		ArrayList<ChangeNotifier> tmpList = globalNotifier.get(notifyAction);

		if(tmpList == null)
		{
			tmpList = new ArrayList<ChangeNotifier>();
			tmpList.add(notifyObject);
			globalNotifier.put(notifyAction, tmpList);
		}
		else
		{
			tmpList.add(notifyObject);
		}
	}

	public void removeChangeNotifier(ChangeNotifier notifyObject)
	{
		for(ArrayList<ChangeNotifier> tmpList : globalNotifier.values())
		{
			if(tmpList.contains(notifyObject))tmpList.remove(notifyObject);
		}
	}

	public void fireChangeHappened(Class<? extends ChangeNotifier> notifyAction)
	{
		ArrayList<ChangeNotifier> tmpList = globalNotifier.get(notifyAction);

		for(ChangeNotifier tmpNotifier : tmpList)
		{
			tmpNotifier.hasChanged();
		}
	}
}
