/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.gui;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import de.konzept_e.todo.core.CurrentSession;
import de.konzept_e.todo.core.DateFormater;
import de.konzept_e.todo.database.HibernateUtil;
import de.konzept_e.todo.database.entities.Meeting;
import de.konzept_e.todo.database.entities.TodoElement;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
public class TodoElementOverviewGui extends Panel implements GuiElement, ItemClickListener
{
	private final Table table = new Table("Protokollelemente");
	private final Button newButton = new Button("Neu");
	private final Button editButton = new Button("Bearbeiten");
	private final Button searchButton = new Button("Suchen");
	private final Button deleteButton = new Button("Löschen");
	private CurrentSession session;
	private int currentItem = 0;
	private int meetingId = 0;

	public TodoElementOverviewGui(int meetingId)
	{
		setSizeFull();
		this.meetingId = meetingId;

		VerticalLayout splitLayout = new VerticalLayout();
		addComponent(splitLayout);
		splitLayout.setSizeFull();
		splitLayout.setSpacing(true);

		table.setSelectable(true);
		table.setImmediate(true);
		table.setWidth("100%");
		refreshOrLoadData();
		table.setColumnReorderingAllowed(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnHeaders(new String[]
				{
					"Kategorie",
					"Bereich",
					"Thema",
					"Wiedervorlage",
					"Überschrift"
				});
		table.addListener(this);
		splitLayout.addComponent(table);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(newButton);
		buttonLayout.addComponent(searchButton);
		buttonLayout.addComponent(editButton);
		buttonLayout.addComponent(deleteButton);
		splitLayout.addComponent(buttonLayout);
		editButton.addListener(Button.ClickEvent.class, this, "editButtonClick");
		newButton.addListener(Button.ClickEvent.class, this, "newButtonClick");
		searchButton.addListener(Button.ClickEvent.class, this, "searchButtonClick");
		deleteButton.addListener(Button.ClickEvent.class, this, "deleteButtonClick");
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void editButtonClick(ClickEvent event)
	{
		TodoElementGui newGui = new TodoElementGui(true, currentItem, meetingId);
		newGui.init(session);
		session.getAppInstance().addTab(newGui, "Protokollelement bearbeiten", null);

		table.select(table.getNullSelectionItemId());
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void newButtonClick(ClickEvent event)
	{
		TodoElementGui newGui = new TodoElementGui(false, -1, meetingId);
		newGui.init(session);
		session.getAppInstance().addTab(newGui, "Protokollelement erstellen", null);

		table.select(table.getNullSelectionItemId());
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void searchButtonClick(ClickEvent event)
	{
		new SearchDialog(getWindow(), "Protokollelement suchen", "Bitte beliebigen Suchbegriff eingeben.", new SearchDialog.SearchAction()
		{
			public void searchEvent(String searchPhrase)
			{
				session.getAppInstance().showTrayNotification("Suchbegriff (Suche einbauen!): " + searchPhrase);
			}
		});
	}

	public void deleteButtonClick(ClickEvent event)
	{
		new DeleteConfirmDialog(getWindow(), "Wirklich löschen?", "Wollen Sie das Protokollelement wirklich löschen?", new DeleteConfirmDialog.DeleteAction()
		{
			public void deleteEvent()
			{
				Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
				Transaction tx = dbSession.beginTransaction();

				TodoElement todoElement = (TodoElement) dbSession.load(TodoElement.class, currentItem);
				todoElement.setDeleted(true);
				dbSession.update(todoElement);

				tx.commit();
				session.getAppInstance().showCenterNotification("Protokollelement wurde gelöscht.");
			}
		});
	}

	private void refreshOrLoadData()
	{
		IndexedContainer con = new IndexedContainer();
		con.addContainerProperty("Kategorie", String.class, "");
		con.addContainerProperty("Bereich", String.class, "");
		con.addContainerProperty("Thema", String.class, "");
		con.addContainerProperty("Wiedervorlage", DateFormater.class, "");
		con.addContainerProperty("Überschrift", String.class, "");

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();
		@SuppressWarnings("unchecked")
		List<TodoElement> todoElements = dbSession.createQuery("from TodoElement t INNER JOIN FETCH t.status INNER JOIN FETCH t.area INNER JOIN FETCH t.category INNER JOIN FETCH t.institution WHERE t.deleted = false AND t.meeting = ? ORDER BY t.reminderDate DESC").setInteger(0, meetingId).list();

		for (TodoElement todoElement : todoElements)
		{
			Item item = con.addItem(todoElement.getId());
			item.getItemProperty("Kategorie").setValue(todoElement.getCategory().getName());
			item.getItemProperty("Bereich").setValue(todoElement.getArea().getName());
			item.getItemProperty("Thema").setValue("FIXME");
			item.getItemProperty("Wiedervorlage").setValue(new DateFormater(todoElement.getReminderDate(), todoElement.isReminderEnabled()));
			item.getItemProperty("Überschrift").setValue(todoElement.getHeading());
		}

		tx.commit();

		table.setContainerDataSource(con);
	}

	public void itemClick(ItemClickEvent event)
	{
		editButton.setEnabled(true);
		deleteButton.setEnabled(true);
		currentItem = (Integer) event.getItemId();

		if (event.isDoubleClick())
		{
			TodoElementGui newGui = new TodoElementGui(true, currentItem, meetingId);
			newGui.init(session);
			session.getAppInstance().addTab(newGui, "Protokollelement bearbeiten", null);

			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			table.select(table.getNullSelectionItemId());
		}
	}

	public void init(CurrentSession session)
	{
		this.session = session;
	}
}
