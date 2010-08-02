/*
 * $Id$
 */
package de.konzept_e.todo.core;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import de.konzept_e.todo.gui.GuiElement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sven
 */
public class ApplicationMenu extends MenuBar
{
	private final CurrentSession session;
	private final HashMap<String, MenuItem> allMenus = new HashMap<String, MenuItem>();
	private final HashMap<MenuItem, Class<? extends GuiElement>> allMenuGuiElements = new HashMap<MenuItem, Class<? extends GuiElement>>();
	private final HashMap<MenuItem, String> allTabTitles = new HashMap<MenuItem, String>();

	public ApplicationMenu(CurrentSession session)
	{
		this.session = session;
		setWidth("100%");
	}

	public MenuItem addMenu(String title, Resource icon)
	{
		MenuItem tmpItem = addItem(title, icon, null);
		allMenus.put(title, tmpItem);
		return tmpItem;
	}

	public MenuItem addMenuEntry(String title, Resource icon, MenuItem parentMenuEntry, Class<? extends GuiElement> menuGuiElement)
	{
		MenuItem tmpItem = parentMenuEntry.addItem(title, icon, menuCommand);
		allMenuGuiElements.put(tmpItem, menuGuiElement);
		allTabTitles.put(tmpItem, title);
		return tmpItem;
	}
	
	private Command menuCommand = new Command()
	{
		public void menuSelected(MenuItem selectedItem)
		{
			Class<? extends GuiElement> element = allMenuGuiElements.get(selectedItem);
			try
			{
				GuiElement inst = element.newInstance();
				inst.init(session);
				session.getAppInstance().addTab(inst, allTabTitles.get(selectedItem), selectedItem.getIcon());
			} catch (InstantiationException ex)
			{
				Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex)
			{
				Logger.getLogger(ApplicationMenu.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	};
}
