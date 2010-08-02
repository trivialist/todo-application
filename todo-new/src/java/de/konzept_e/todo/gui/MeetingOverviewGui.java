/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.gui;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import de.konzept_e.todo.core.ChangeNotifier;
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
public class MeetingOverviewGui extends Panel implements GuiElement, ItemClickListener, ChangeNotifier
{
	private final Table table = new Table("Sitzungen");
	private final Button newButton = new Button("Neu");
	private final Button editButton = new Button("Bearbeiten");
	private final Button searchButton = new Button("Suchen");
	private final Button deleteButton = new Button("Löschen");
	private CurrentSession session;
	private int currentItem = 0;

	public MeetingOverviewGui()
	{
		setSizeFull();

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
					"Datum",
					"Ort",
					"Sitzungsart",
					"Protokollant"
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
		MeetingGui newGui = new MeetingGui(true, currentItem);
		newGui.init(session);
		session.getAppInstance().addTab(newGui, "Sitzung bearbeiten", null);

		table.select(table.getNullSelectionItemId());
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void newButtonClick(ClickEvent event)
	{
		MeetingGui newGui = new MeetingGui(false, -1);
		newGui.init(session);
		session.getAppInstance().addTab(newGui, "Sitzung anlegen", null);

		table.select(table.getNullSelectionItemId());
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void searchButtonClick(ClickEvent event)
	{
		new SearchDialog(getWindow(), "Sitzung suchen", "Bitte beliebigen Suchbegriff eingeben.", new SearchDialog.SearchAction()
		{
			public void searchEvent(String searchPhrase)
			{
				session.getAppInstance().showTrayNotification("Suchbegriff (Suche einbauen!): " + searchPhrase);
			}
		});
	}

	public void deleteButtonClick(ClickEvent event)
	{
		new DeleteConfirmDialog(getWindow(), "Wirklich löschen?", "Wollen Sie die Sitzung mit all ihren Unterelementen wirklich löschen?", new DeleteConfirmDialog.DeleteAction()
		{
			public void deleteEvent()
			{
				Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
				Transaction tx = dbSession.beginTransaction();

				Meeting meeting = (Meeting) dbSession.load(Meeting.class, currentItem);
				meeting.setDeleted(true);
				dbSession.update(meeting);
				@SuppressWarnings("unchecked")
				List<TodoElement> todoElements = dbSession.createQuery("from TodoElement t where t.meeting = ?").setInteger(0, meeting.getId()).list();

				for (TodoElement tmpElement : todoElements)
				{
					tmpElement.setDeleted(true);
					dbSession.update(tmpElement);
				}

				tx.commit();
				session.getAppInstance().showCenterNotification("Sitzung wurde gelöscht.");
			}
		});
	}

	private void refreshOrLoadData()
	{
		IndexedContainer con = new IndexedContainer();
		con.addContainerProperty("Datum", DateFormater.class, "");
		con.addContainerProperty("Ort", String.class, "");
		con.addContainerProperty("Sitzungsart", String.class, "");
		con.addContainerProperty("Protokollant", String.class, "");

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Meeting> meetings = dbSession.createQuery("from Meeting m INNER JOIN FETCH m.meetingType INNER JOIN FETCH m.minuteTaker WHERE m.deleted = false ORDER BY m.date DESC").list();

		for (Meeting meeting : meetings)
		{
			Item item = con.addItem(meeting.getId());
			item.getItemProperty("Datum").setValue(new DateFormater(meeting.getDate()));
			item.getItemProperty("Ort").setValue(meeting.getPlace());
			item.getItemProperty("Sitzungsart").setValue(meeting.getMeetingType().getName());
			item.getItemProperty("Protokollant").setValue(meeting.getMinuteTaker().getLastname() + ", " + meeting.getMinuteTaker().getFirstname());
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
			TodoElementOverviewGui newGui = new TodoElementOverviewGui(currentItem);
			newGui.init(session);
			session.getAppInstance().addTab(newGui, "Protokollelement-Übersicht", null);

			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
			table.select(table.getNullSelectionItemId());
		}
	}

	public void init(CurrentSession session)
	{
		this.session = session;
		session.registerChangeNotifier(MeetingOverviewGui.class, this);
	}

	public void hasChanged()
	{
		refreshOrLoadData();
	}
}
