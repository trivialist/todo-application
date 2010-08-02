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
import de.konzept_e.todo.database.entities.Institution;
import de.konzept_e.todo.database.entities.Topic;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
public class InstitutionGui extends Panel implements GuiElement, ItemClickListener
{
	private final Table table = new Table("Institutionen:");
	private CurrentSession session;
	private final Form form = new Form();
	private final Button saveButton = new Button("Speichern", form, "commit");
	private final Button newButton = new Button("Neu");
	private int currentItem = 0;
	private Institution currentInstitution;

	public InstitutionGui()
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
					"Name"
				});
		table.addListener(this);
		splitLayout.addComponent(table);

		form.setCaption("Daten bearbeiten...");
		form.setDescription("Bitte veränderte bzw. neue Daten eingeben und dann auf Speichern klicken.");
		form.addField("Name", new TextField("Name"));
		form.getField("Name").setEnabled(false);
		form.getField("Name").setRequired(true);
		form.getField("Name").setRequiredError("Das Feld 'Name' darf nicht leer sein.");
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
		saveButton.setEnabled(true);

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();

		Transaction tx = dbSession.beginTransaction();
		currentInstitution = (Institution) dbSession.load(Institution.class, currentItem);

		((TextField) form.getField("Name")).setValue(currentInstitution.getName());

		tx.commit();
	}

	public void saveButtonClick(ClickEvent event)
	{
		currentInstitution.setName((String) ((TextField) form.getField("Name")).getValue());

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();
		dbSession.saveOrUpdate(currentInstitution);
		tx.commit();

		session.getAppInstance().showCenterNotification("Institution wurde gespeichert bzw. aktualisiert.");

		refreshOrLoadData();

		//notify other tabs
	}

	public void newButtonClick(Button.ClickEvent event)
	{
		currentInstitution = new Institution();
		((TextField)form.getField("Name")).setValue("");
		table.select(table.getNullSelectionItemId());
		form.getField("Name").setEnabled(true);
		saveButton.setEnabled(true);
	}

	private void refreshOrLoadData()
	{
		IndexedContainer con = new IndexedContainer();
		con.addContainerProperty("Name", String.class, "");

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Institution> institutions = dbSession.createQuery("from Institution AS institution ORDER BY institution.name ASC").list();

		for (Institution institution : institutions)
		{
			Item item = con.addItem(institution.getId());
			item.getItemProperty("Name").setValue(institution.getName());
		}

		tx.commit();

		table.setContainerDataSource(con);
	}
}
