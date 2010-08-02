/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.gui;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import de.konzept_e.todo.core.ChangeNotifier;
import de.konzept_e.todo.core.CurrentSession;
import de.konzept_e.todo.database.HibernateUtil;
import de.konzept_e.todo.database.entities.Area;
import de.konzept_e.todo.database.entities.AreaTopic;
import de.konzept_e.todo.database.entities.AreaTopic;
import de.konzept_e.todo.database.entities.Category;
import de.konzept_e.todo.database.entities.Employee;
import de.konzept_e.todo.database.entities.Institution;
import de.konzept_e.todo.database.entities.Meeting;
import de.konzept_e.todo.database.entities.MeetingType;
import de.konzept_e.todo.database.entities.Status;
import de.konzept_e.todo.database.entities.TodoElement;
import de.konzept_e.todo.database.entities.Topic;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
public class TodoElementGui extends Panel implements GuiElement, ChangeNotifier, Property.ValueChangeListener
{
	private CurrentSession session;
	private final TodoElement editElement;
	private final boolean editMode;
	private final Form form = new Form();
	private final Button saveButton = new Button("Speichern");
	private final ComboBox areaComboField = new ComboBox("Bereich");
	private final TextField headingField = new TextField("Überschrift");
	private final TextField contentField = new TextField("Inhalt");
	private final ComboBox categoryComboField = new ComboBox("Kategorie");
	private final ComboBox institutionComboField = new ComboBox("Institution");
	private final ComboBox statusComboField = new ComboBox("Status");
	private final ComboBox reminderMeetingTypeComboField = new ComboBox("WV Sitzungstyp");
	private final ComboBox areaTopicComboField = new ComboBox("Bereich");
	private final ComboBox topicComboField = new ComboBox("Thema");
	private final CheckBox reminderEnabledField = new CheckBox("Wiedervorlage");
	private final PopupDateField reminderDateField = new PopupDateField("WV Datum");
	private final ComboBox employeeComboField = new ComboBox("Mitarbeiter");
	private final IndexedContainer employeeContainer = new IndexedContainer();
	private final IndexedContainer responsibleContainer = new IndexedContainer();
	private final IndexedContainer involvedContainer = new IndexedContainer();
	private final IndexedContainer areaContainer = new IndexedContainer();
	private final IndexedContainer topicContainer = new IndexedContainer();
	private final Button addResponsibleButton = new Button("Hinzufügen");
	private final Button addInvolvedButton = new Button("Hinzufügen");
	private final Button removeInvolvedButton = new Button("Entfernen");
	private final Button removeResponsibleButton = new Button("Entfernen");
	private final Table responsibleTable = new Table("Verantwortliche");
	private final Table involvedTable = new Table("Beteiligte");
	private int meetingId;
	private int currentInvolvedItem;
	private int currentResponsibleItem;

	public TodoElementGui(boolean editMode, int editId, int meetingId)
	{
		setSizeFull();
		this.editMode = editMode;
		this.meetingId = meetingId;

		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();

		if (editId == -1)
		{
			editElement = new TodoElement();
			editElement.setArea(Area.getDefaultElement(dbSession));
			editElement.setCategory(Category.getDefaultElement(dbSession));
			editElement.setInstitution(Institution.getDefaultElement(dbSession));
			editElement.setReminderType(MeetingType.getDefaultElement(dbSession));
			editElement.setStatus(Status.getDefaultElement(dbSession));
			editElement.setResponsibles(new ArrayList<Employee>());
			editElement.setMeeting((Meeting) dbSession.load(Meeting.class, meetingId));
			editElement.setContent("");
			editElement.setCopyReason("");
			editElement.setCopyOf(null);
			editElement.setDeleted(false);
			editElement.setHeading("");
			editElement.setInvolved(new ArrayList<Employee>());
			editElement.setReminderDate(new Date());
			editElement.setReminderEnabled(false);
			editElement.setAreaTopic(AreaTopic.getDefaultElement(dbSession));
		}
		else
		{
			editElement = (TodoElement) dbSession.load(TodoElement.class, editId);
		}
		
		//area
		@SuppressWarnings("unchecked")
		List<Area> areas = dbSession.createQuery("FROM Area").list();
		for (Area currentElement : areas)
		{
			areaComboField.addItem(currentElement);
		}
		areaComboField.select(editElement.getArea());
		areaComboField.setNullSelectionAllowed(false);
		form.addField("Bereich", areaComboField);

		//heading
		headingField.setValue(editElement.getHeading());
		headingField.setColumns(30);
		form.addField("Überschrift", headingField);

		//content
		contentField.setRows(5);
		contentField.setColumns(30);
		contentField.setValue(editElement.getContent());
		form.addField("Inhalt", contentField);

		//category
		@SuppressWarnings("unchecked")
		List<Category> categories = dbSession.createQuery("FROM Category").list();
		for (Category currentElement : categories)
		{
			categoryComboField.addItem(currentElement);
		}
		categoryComboField.select(editElement.getCategory());
		categoryComboField.setNullSelectionAllowed(false);
		form.addField("Kategorie", categoryComboField);

		//institution
		@SuppressWarnings("unchecked")
		List<Institution> institutions = dbSession.createQuery("FROM Institution").list();
		for (Institution currentElement : institutions)
		{
			institutionComboField.addItem(currentElement);
		}
		institutionComboField.select(editElement.getInstitution());
		institutionComboField.setNullSelectionAllowed(false);
		form.addField("Institution", institutionComboField);
		
		//status
		@SuppressWarnings("unchecked")
		List<Status> stati = dbSession.createQuery("FROM Status").list();
		for (Status currentElement : stati)
		{
			statusComboField.addItem(currentElement);
		}
		statusComboField.select(editElement.getStatus());
		statusComboField.setNullSelectionAllowed(false);
		form.addField("Status", statusComboField);

		//reminder enabled
		reminderEnabledField.setValue((Boolean) editElement.isReminderEnabled());
		reminderEnabledField.addListener(Button.ClickEvent.class, this, "reminderCheckboxClick");
		reminderEnabledField.setImmediate(true);
		form.addField("Wiedervorlage aktiviert", reminderEnabledField);

		//reminder meeting type
		@SuppressWarnings("unchecked")
		List<MeetingType> reminderMeetingTypes = dbSession.createQuery("FROM MeetingType").list();
		for (MeetingType currentElement : reminderMeetingTypes)
		{
			reminderMeetingTypeComboField.addItem(currentElement);
		}
		reminderMeetingTypeComboField.select(editElement.getReminderType());
		reminderMeetingTypeComboField.setNullSelectionAllowed(false);
		form.addField("WV Sitzungsart", reminderMeetingTypeComboField);

		//reminder date
		reminderDateField.setValue(editElement.getReminderDate());
		reminderDateField.setDateFormat("dd.MM.yyyy");
		form.addField("WV Datum", reminderDateField);

		//area topic
		@SuppressWarnings("unchecked")
		List<AreaTopic> areaTopics = dbSession.createQuery("from AreaTopic at INNER JOIN FETCH at.area a ORDER BY a.name ASC").list();
		for(AreaTopic currentAreaTopic : areaTopics)
		{
			Area area = currentAreaTopic.getArea();
			if(areaContainer.containsId(area) == false)
			{
				areaContainer.addItem(area);
			}
		}
		areaTopicComboField.setContainerDataSource(areaContainer);
		areaTopicComboField.setNullSelectionAllowed(false);
		areaTopicComboField.addListener(this);
		areaTopicComboField.setImmediate(true);
		form.addField("Bereich", areaTopicComboField);

		topicComboField.setEnabled(false);
		topicComboField.setImmediate(true);
		topicComboField.setContainerDataSource(topicContainer);
		topicComboField.setNullSelectionAllowed(false);
		form.addField("Thema", topicComboField);
		
		//employee combo box
		@SuppressWarnings("unchecked")
		List<Employee> employees = dbSession.createQuery("FROM Employee ORDER BY lastname ASC, firstname ASC").list();
		for (Employee currentElement : employees)
		{
			employeeContainer.addItem(currentElement);
		}
		employeeComboField.setContainerDataSource(employeeContainer);
		employeeComboField.setNullSelectionAllowed(false);
		form.addField("Mitarbeiter", employeeComboField);

		//responsible
		responsibleContainer.addContainerProperty("Name", Employee.class, "");
		responsibleTable.setContainerDataSource(responsibleContainer);
		responsibleTable.setColumnHeaders(new String[]
				{
					"Name"
				});
		responsibleTable.setHeight("150px");
		responsibleTable.setSelectable(true);
		responsibleTable.setImmediate(true);
		responsibleTable.setWidth("25%");
		responsibleTable.setColumnReorderingAllowed(true);
		responsibleTable.setColumnCollapsingAllowed(true);
		responsibleTable.addListener(new ItemClickListener()
		{
			public void itemClick(ItemClickEvent event)
			{
				//item in table has been clicked
				removeResponsibleButton.setEnabled(true);
				currentResponsibleItem = (Integer) event.getItemId();
			}
		});
		addResponsibleButton.addListener(Button.ClickEvent.class, this, "addResponsibleClick");
		form.addField("", addResponsibleButton);
		removeResponsibleButton.setImmediate(true);
		removeResponsibleButton.setEnabled(false);
		removeResponsibleButton.addListener(Button.ClickEvent.class, this, "removeResponsibleClick");
		form.addField("", removeResponsibleButton);
		form.addField("Verantwortliche", responsibleTable);

		//involved
		involvedContainer.addContainerProperty("Name", Employee.class, "");
		involvedTable.setContainerDataSource(involvedContainer);
		involvedTable.setColumnHeaders(new String[]
				{
					"Name"
				});
		involvedTable.setHeight("150px");
		involvedTable.setSelectable(true);
		involvedTable.setImmediate(true);
		involvedTable.setWidth("25%");
		involvedTable.setColumnReorderingAllowed(true);
		involvedTable.setColumnCollapsingAllowed(true);
		involvedTable.addListener(new ItemClickListener()
		{
			public void itemClick(ItemClickEvent event)
			{
				//item in table has been clicked
				removeInvolvedButton.setEnabled(true);
				currentInvolvedItem = (Integer) event.getItemId();
			}
		});
		addInvolvedButton.addListener(Button.ClickEvent.class, this, "addInvolvedClick");
		form.addField("", addInvolvedButton);
		removeInvolvedButton.setImmediate(true);
		removeInvolvedButton.setEnabled(false);
		removeInvolvedButton.addListener(Button.ClickEvent.class, this, "removeInvolvedClick");
		form.addField("", removeInvolvedButton);
		form.addField("Beteiligte", involvedTable);

		setReminderInputsEnabled();

		//set form options
		saveButton.addListener(Button.ClickEvent.class, this, "saveButtonClick");
		form.getFooter().addComponent(saveButton);
		form.setCaption("Daten bearbeiten...");
		form.setDescription("Bitte veränderte bzw. neue Daten eingeben und dann auf Speichern klicken.");
		addComponent(form);

		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public void removeResponsibleClick(ClickEvent event)
	{
		//remove employee from participiants list
		responsibleContainer.removeItem(currentResponsibleItem);
		removeResponsibleButton.setEnabled(false);
		responsibleTable.select(responsibleTable.getNullSelectionItemId());
	}

	@SuppressWarnings("unchecked")
	public void removeInvolvedClick(ClickEvent event)
	{
		//remove employee from participiants list
		involvedContainer.removeItem(currentInvolvedItem);
		removeInvolvedButton.setEnabled(false);
		involvedTable.select(involvedTable.getNullSelectionItemId());
	}

	@SuppressWarnings("unchecked")
	public void addResponsibleClick(ClickEvent event)
	{
		//add selected employee from combo box to table
		Employee selectedEmployee = (Employee) employeeComboField.getValue();

		if (selectedEmployee != null && responsibleContainer.getItem(selectedEmployee.getId()) == null)
		{
			Item item = responsibleContainer.addItem(selectedEmployee.getId());
			item.getItemProperty("Name").setValue(selectedEmployee);
		}
	}

	@SuppressWarnings("unchecked")
	public void addInvolvedClick(ClickEvent event)
	{
		//add selected employee from combo box to table
		Employee selectedEmployee = (Employee) employeeComboField.getValue();

		if (selectedEmployee != null && involvedContainer.getItem(selectedEmployee.getId()) == null)
		{
			Item item = involvedContainer.addItem(selectedEmployee.getId());
			item.getItemProperty("Name").setValue(selectedEmployee);
		}
	}

	@SuppressWarnings("unchecked")
	public void saveButtonClick(ClickEvent event)
	{
		session.getAppInstance().showTrayNotification("Speichern hier hin!");
	}

	@SuppressWarnings("unchecked")
	public void reminderCheckboxClick(ClickEvent event)
	{
		setReminderInputsEnabled();
	}

	private void setReminderInputsEnabled()
	{
		if ((Boolean) reminderEnabledField.getValue() == true)
		{
			reminderDateField.setEnabled(true);
			reminderMeetingTypeComboField.setEnabled(true);
		}
		else
		{
			reminderDateField.setEnabled(false);
			reminderMeetingTypeComboField.setEnabled(false);
		}
	}

	public void init(CurrentSession session)
	{
		this.session = session;
	}

	public void hasChanged()
	{
		session.getAppInstance().showTrayNotification("sdfsdfsd");
	}

	public void valueChange(ValueChangeEvent event)
	{
		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();
		topicContainer.removeAllItems();
		Area selectedArea = (Area) event.getProperty().getValue();

		List<AreaTopic> areaTopics = dbSession.createQuery("from AreaTopic at INNER JOIN FETCH at.topic t WHERE at.area = ? ORDER BY t.name ASC").setInteger(0, selectedArea.getId()).list();
		for(AreaTopic currentAreaTopic : areaTopics)
		{
			Topic topic = currentAreaTopic.getTopic();
			if(topicContainer.containsId(topic) == false)
			{
				topicContainer.addItem(topic);
			}
		}

		topicComboField.setEnabled(true);
		topicComboField.setNullSelectionItemId(topicComboField.getNullSelectionItemId());
		topicComboField.setValue("");
		tx.commit();
	}
}
