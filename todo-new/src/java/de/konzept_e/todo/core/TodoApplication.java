/*
 * TodoApplication.java
 *
 * Created on 16. Mai 2010, 12:10
 */
package de.konzept_e.todo.core;

import com.vaadin.Application;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer.ComponentDetachEvent;
import com.vaadin.ui.ComponentContainer.ComponentDetachListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.Notification;
import de.konzept_e.todo.gui.AboutGui;
import de.konzept_e.todo.gui.AreaGui;
import de.konzept_e.todo.gui.CategoryGui;
import de.konzept_e.todo.gui.GuiElement;
import de.konzept_e.todo.gui.InstitutionGui;
import de.konzept_e.todo.gui.MeetingOverviewGui;
import de.konzept_e.todo.gui.MeetingTypeGui;
import de.konzept_e.todo.gui.MigrationGui;
import de.konzept_e.todo.gui.TopicGui;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** 
 *
 * @author sven
 *
 * @todo all search functions
 * @todo settings into web context
 * @todo change -> refresh
 * @todo documentation open menu entry
 * @todo form layout (length of fields)
 *
 */
public class TodoApplication extends Application implements ComponentDetachListener
{
	private final TabSheet tabSheet = new TabSheet();
	private final CurrentSession session = new CurrentSession(this);
	private final VerticalLayout mainVerticalLayout = new VerticalLayout();
	private final ApplicationMenu mainMenu = new ApplicationMenu(session);
	private final ArrayList<GuiElement> openedTabs = new ArrayList<GuiElement>();

	@Override
	public void init()
	{
		setTheme("reindeer");
		setMainWindow(new Window("TodoApplication", mainVerticalLayout));

		//HttpSession httpSession = ((WebApplicationContext) getContext()).getHttpSession();

		//initialize global settings object
		try
		{
			SettingsUtil.loadFromXML(new File(getContext().getBaseDirectory().getAbsolutePath() + "/WEB-INF/TodoApplicationSettings.xml"));
		} catch (IOException ex)
		{
			Logger.getLogger(TodoApplication.class.getName()).log(Level.SEVERE, null, ex);
			getMainWindow().showNotification("Error", "Application was unable to load settings to run properly.", Notification.TYPE_ERROR_MESSAGE);
			return;
		}

		//initialize global logger object
		try
		{
			initLogger();
		} catch (Exception ex)
		{
			Logger.getLogger(TodoApplication.class.getName()).log(Level.SEVERE, null, ex);
			getMainWindow().showNotification("Error", "Application was unable to initialize the logger to run properly.", Notification.TYPE_ERROR_MESSAGE);
			return;
		}

		//initialize the layout
		initLayout();
	}

	private void initLayout()
	{
		//add menu and tab sheet
		mainVerticalLayout.setSizeFull();
		mainVerticalLayout.addComponent(mainMenu);
		mainVerticalLayout.addComponent(tabSheet);
		mainVerticalLayout.setExpandRatio(tabSheet, 1.0f);
		tabSheet.setSizeFull();
		tabSheet.setImmediate(true);

		//tab sheet close listener
		tabSheet.addListener(this);

		MenuItem menu1 = mainMenu.addMenu("Stammdaten", null);
		mainMenu.addMenuEntry("Bereiche", null, menu1, AreaGui.class);
		mainMenu.addMenuEntry("Kategorien", null, menu1, CategoryGui.class);
		mainMenu.addMenuEntry("Themen", null, menu1, TopicGui.class);
		mainMenu.addMenuEntry("Institutionen", null, menu1, InstitutionGui.class);
		mainMenu.addMenuEntry("Sitzungsarten", null, menu1, MeetingTypeGui.class);

		MenuItem menu2 = mainMenu.addMenu("Hilfe", null);
		mainMenu.addMenuEntry("Dokumentation aufrufen", null, menu2, null);
		mainMenu.addMenuEntry("Über diese Anwendung", null, menu2, AboutGui.class);
		mainMenu.addMenuEntry("ALTDATEN MIGRIEREN", null, menu2, MigrationGui.class);

		MeetingOverviewGui tmpGui = new MeetingOverviewGui();
		tmpGui.init(session);
		addTab(tmpGui, "Sitzungs-Übersicht", null, false);
	}

	private void initLogger() throws Exception
	{
		Logger logger = Logger.getLogger(TodoApplication.class.getName());
		FileHandler fileHandler = new FileHandler(SettingsUtil.getProperty("LoggerLogfilePath"), true);
		logger.addHandler(fileHandler);
		logger.setLevel(Level.ALL);
		SimpleFormatter logfileFormatter = new SimpleFormatter();
		fileHandler.setFormatter(logfileFormatter);
	}

	public void showTrayNotification(String message)
	{
		getMainWindow().showNotification(message, Window.Notification.TYPE_TRAY_NOTIFICATION);
	}

	public void showCenterNotification(String message)
	{
		getMainWindow().showNotification(message, Window.Notification.TYPE_HUMANIZED_MESSAGE);
	}

	public void addTab(GuiElement component, String caption, Resource icon)
	{
		addTab(component, caption, icon, true);
	}

	public void addTab(GuiElement component, String caption, Resource icon, boolean closeable)
	{
		Tab newTab = tabSheet.addTab(component, caption, icon);
		newTab.setClosable(closeable);
		tabSheet.setSelectedTab(component);
		openedTabs.add(component);
	}

	public void componentDetachedFromContainer(ComponentDetachEvent event)
	{
		//remove ChangeNotifier from global list when tab is beeing closed
		if (event.getDetachedComponent() instanceof ChangeNotifier)
		{
			session.removeChangeNotifier((ChangeNotifier) event.getDetachedComponent());
		}

		//set last tab as the selected one
		openedTabs.remove((GuiElement)event.getDetachedComponent());
		tabSheet.setSelectedTab(openedTabs.get(openedTabs.size()-1));
	}
}
