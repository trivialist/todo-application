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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import de.konzept_e.todo.core.CurrentSession;
import de.konzept_e.todo.database.HibernateUtil;
import de.konzept_e.todo.database.entities.Employee;
import de.konzept_e.todo.database.entities.Meeting;
import de.konzept_e.todo.database.entities.MeetingType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
public class MeetingGui extends Panel implements GuiElement, ItemClickListener
{
	private CurrentSession session;
	private final Meeting editElement;
	private final boolean editMode;
	private final Form form = new Form();
	private final Button saveButton = new Button("Speichern");
	private final Button removeButton = new Button("Entfernen");
	private final IndexedContainer participiantsContainer = new IndexedContainer();
	private final IndexedContainer employeeContainer = new IndexedContainer();
	private final ComboBox employeeComboField = new ComboBox("Mitarbeiter");
	private final Table participiants = new Table("Teilnehmer");
	private final TextField placeField = new TextField("Ort");
	private final PopupDateField dateField = new PopupDateField("Datum");
	private final TextField otherParticipiants = new TextField("Andere Teilnehmer");
	private final ComboBox meetingTypeField = new ComboBox("Sitzungsart");
	private final ComboBox minuteTakerField = new ComboBox("Protokollant");
	private int currentItem = 0;

	public MeetingGui(boolean editMode, int editId)
	{
		setSizeFull();
		this.editMode = editMode;
		
		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();

		if(editId == -1)
		{
			editElement = new Meeting();
			editElement.setMeetingType(MeetingType.getDefaultElement(dbSession));
			editElement.setMinuteTaker(Employee.getDefaultElement(dbSession));
			editElement.setParticipiants(new ArrayList<Employee>());
			editElement.setDeleted(false);
			editElement.setDate(new Date());
			editElement.setPlace("");
			editElement.setOtherParticipiants("");
		}
		else
		{
			editElement = (Meeting) dbSession.load(Meeting.class, editId);
		}

		//Place
		placeField.setValue(editElement.getPlace());
		placeField.setColumns(25);
		form.addField("Ort", placeField);

		//Date
		dateField.setValue(editElement.getDate());
		dateField.setDateFormat("dd.MM.yyyy");
		form.addField("Datum", dateField);

		//other participiants
		otherParticipiants.setValue(editElement.getOtherParticipiants());
		otherParticipiants.setRows(2);
		otherParticipiants.setColumns(25);
		form.addField("Andere Teilnehmer", otherParticipiants);

		//meeting type
		@SuppressWarnings("unchecked")
		List<MeetingType> meetings = dbSession.createQuery("FROM MeetingType").list();
		for(MeetingType currentElement : meetings)
		{
			meetingTypeField.addItem(currentElement);
		}
		meetingTypeField.select(editElement.getMeetingType());
		meetingTypeField.setNullSelectionAllowed(false);
		form.addField("Sitzungsart", meetingTypeField);

		//minute taker
		@SuppressWarnings("unchecked")
		List<Employee> employees = dbSession.createQuery("FROM Employee ORDER BY lastname ASC, firstname ASC").list();
		for(Employee currentElement : employees)
		{
			minuteTakerField.addItem(currentElement);
		}
		minuteTakerField.select(editElement.getMinuteTaker());
		minuteTakerField.setNullSelectionAllowed(false);
		form.addField("Protokollant", minuteTakerField);

		//participiants
		for(Employee currentElement : employees)
		{
			employeeContainer.addItem(currentElement);
		}
		employeeComboField.setContainerDataSource(employeeContainer);
		employeeComboField.setNullSelectionAllowed(false);
		form.addField("Mitarbeiter", employeeComboField);

		//button to add participiant to list
		Button addButton = new Button("Hinzufügen");
		addButton.addListener(Button.ClickEvent.class, this, "addButtonClick");
		form.addField("", addButton);

		//remove button to remove participiants
		removeButton.addListener(Button.ClickEvent.class, this, "removeButtonClick");
		removeButton.setEnabled(false);
		form.addField("", removeButton);

		//table for participiants
		participiants.setSelectable(true);
		participiants.setImmediate(true);
		participiants.setWidth("25%");
		participiants.setColumnReorderingAllowed(true);
		participiants.setColumnCollapsingAllowed(true);
		participiantsContainer.addContainerProperty("Name", Employee.class, "");
		participiants.setContainerDataSource(participiantsContainer);
		participiants.setColumnHeaders(new String[]
				{
					"Name"
				});
		participiants.addListener(this);
		participiants.setHeight("150px");
		participiants.addListener(this);
		form.addField("Teilnehmer", participiants);

		//initialize table from loaded data
		for(Employee tmpEmployee : editElement.getParticipiants())
		{
			Item item = participiantsContainer.addItem(tmpEmployee.getId());
			item.getItemProperty("Name").setValue(tmpEmployee);
		}

		//set form options
		form.getFooter().addComponent(saveButton);
		form.setCaption("Daten bearbeiten...");
		form.setDescription("Bitte veränderte bzw. neue Daten eingeben und dann auf Speichern klicken.");
		addComponent(form);

		saveButton.addListener(Button.ClickEvent.class, this, "saveButtonClick");

		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public void saveButtonClick(ClickEvent event)
	{
		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();

		//update list of selected employees
		List<Employee> employees = new ArrayList<Employee>();
		for(Integer employeeId : (Collection<Integer>)participiantsContainer.getItemIds())
		{
			Item item = participiantsContainer.getItem(employeeId);
			employees.add((Employee)item.getItemProperty("Name").getValue());
		}

		//update modified fields
		editElement.setDate((Date)dateField.getValue());
		editElement.setPlace((String)placeField.getValue());
		editElement.setMinuteTaker((Employee)minuteTakerField.getValue());
		editElement.setMeetingType((MeetingType)meetingTypeField.getValue());
		editElement.setParticipiants(employees);
		editElement.setOtherParticipiants((String)otherParticipiants.getValue());

		//save for update object
		if(editMode == false)
		{
			dbSession.save(editElement);
		}
		else
		{
			dbSession.update(editElement);
		}

		//show notification and close
		session.getAppInstance().showCenterNotification("Sitzung wurde gespeichert.");
		//todo close window/tab
		tx.commit();
		
		session.fireChangeHappened(MeetingOverviewGui.class);
	}

	public void addButtonClick(ClickEvent event)
	{
		//add selected employee from combo box to table
		Employee selectedEmployee = (Employee) employeeComboField.getValue();

		if(selectedEmployee != null && participiantsContainer.getItem(selectedEmployee.getId()) == null)
		{
			Item item = participiantsContainer.addItem(selectedEmployee.getId());
			item.getItemProperty("Name").setValue(selectedEmployee);
			employeeComboField.setValue(null);
		}
	}

	public void removeButtonClick(ClickEvent event)
	{
		//remove employee from participiants list
		participiantsContainer.removeItem(currentItem);
		removeButton.setEnabled(false);
		participiants.select(participiants.getNullSelectionItemId());
	}

	/**
	 * inhertited method
	 * 
	 * @param session
	 */
	public void init(CurrentSession session)
	{
		this.session = session;
	}

	public void itemClick(ItemClickEvent event)
	{
		//item in table has been clicked
		removeButton.setEnabled(true);
		currentItem = (Integer)event.getItemId();
	}
}