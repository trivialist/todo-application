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
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import de.konzept_e.todo.core.CurrentSession;
import de.konzept_e.todo.database.HibernateUtil;
import de.konzept_e.todo.database.entities.Area;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
public class AreaGui extends Panel implements GuiElement, ItemClickListener
{
	private final Table table = new Table("Bereiche:");
	private CurrentSession session;
	private final Form form = new Form();
	private final Button saveButton = new Button("Speichern", form, "commit");
	private final Button newButton = new Button("Neu");
	private int currentItem = 0;
	private Area currentArea;

	public AreaGui()
	{
		setSizeFull();

		HorizontalLayout splitLayout = new HorizontalLayout();
		addComponent(splitLayout);
		splitLayout.setSizeFull();
		splitLayout.setSpacing(true);

		saveButton.addListener(Button.ClickEvent.class, this, "saveButtonClick");
		newButton.addListener(Button.ClickEvent.class, this, "newButtonClick");

		table.setWidth("400px");
		table.setSelectable(true);
		table.setImmediate(true);
		refreshOrLoadData();
		table.setColumnReorderingAllowed(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnHeaders(new String[]
				{
					"Name", "Beschreibung"
				});
		table.addListener(this);
		splitLayout.addComponent(table);

		form.setCaption("Daten bearbeiten...");
		form.setDescription("Bitte veränderte bzw. neue Daten eingeben und dann auf Speichern klicken.");
		form.addField("Name", new TextField("Name"));
		form.getField("Name").setEnabled(false);
		form.getField("Name").setRequired(true);
		form.getField("Name").setRequiredError("Das Feld 'Name' darf nicht leer sein.");
		form.addField("Beschreibung", new TextField("Beschreibung"));
		form.getField("Beschreibung").setEnabled(false);
		saveButton.setEnabled(false);
		form.getFooter().addComponent(newButton);
		form.getFooter().addComponent(saveButton);
		form.setSizeFull();
		splitLayout.addComponent(form);
		splitLayout.setExpandRatio(form, 1.0f);
	}

	public void init(CurrentSession session)
	{
		this.session = session;
	}

	public void itemClick(ItemClickEvent event)
	{
		currentItem = (Integer) event.getItemId();

		form.getField("Name").setEnabled(true);
		form.getField("Beschreibung").setEnabled(true);
		saveButton.setEnabled(true);

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = dbSession.beginTransaction();
		currentArea = (Area) dbSession.load(Area.class, currentItem);

		((TextField) form.getField("Name")).setValue(currentArea.getName());
		((TextField) form.getField("Beschreibung")).setValue(currentArea.getDescription());

		tx.commit();
	}

	public void saveButtonClick(ClickEvent event)
	{
		currentArea.setName((String) ((TextField) form.getField("Name")).getValue());
		currentArea.setDescription((String) ((TextField) form.getField("Beschreibung")).getValue());

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();
		dbSession.saveOrUpdate(currentArea);
		tx.commit();

		session.getAppInstance().showCenterNotification("Bereich wurde gespeichert bzw. aktualisiert.");

		refreshOrLoadData();

		//notify other tabs
	}

	public void newButtonClick(Button.ClickEvent event)
	{
		currentArea = new Area();
		((TextField)form.getField("Name")).setValue("");
		((TextField)form.getField("Beschreibung")).setValue("");
		table.select(table.getNullSelectionItemId());
		form.getField("Name").setEnabled(true);
		form.getField("Beschreibung").setEnabled(true);
		saveButton.setEnabled(true);
	}

	private void refreshOrLoadData()
	{
		IndexedContainer con = new IndexedContainer();
		con.addContainerProperty("Name", String.class, "");
		con.addContainerProperty("Beschreibung", String.class, "");

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Area> areas = dbSession.createQuery("from Area AS area ORDER BY area.name ASC").list();

		for (Area area : areas)
		{
			Item item = con.addItem(area.getId());
			item.getItemProperty("Name").setValue(area.getName());
			item.getItemProperty("Beschreibung").setValue(area.getDescription());
		}

		tx.commit();
		
		table.setContainerDataSource(con);
	}
}
