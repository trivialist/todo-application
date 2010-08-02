/*
 * $Id$
 */
package de.konzept_e.todo.gui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import de.konzept_e.todo.core.CurrentSession;
import de.konzept_e.todo.database.HibernateUtil;
import de.konzept_e.todo.database.entities.Employee;
import de.konzept_e.todo.database.entities.Meeting;
import de.konzept_e.todo.database.entities.MeetingEmployee;
import de.konzept_e.todo.database.entities.TodoElementInvEmployee;
import de.konzept_e.todo.database.entities.TodoElementRespEmployee;
import de.konzept_e.todo.database.entities.TodoElement;
import java.util.List;
import java.util.StringTokenizer;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sven
 */
public class MigrationGui extends Panel implements GuiElement
{
	private CurrentSession session;
	private final TextField output = new TextField("Debug-Ausgabe:");

	public MigrationGui()
	{
		setSizeFull();

		output.setColumns(35);
		output.setRows(10);
		addComponent(output);

		Button button = new Button("Alle M:N Daten migrieren");
		button.addListener(Button.ClickEvent.class, this, "migrateButtonClick");
		addComponent(button);
	}

	public void migrateButtonClick(ClickEvent event)
	{
		String skipped1 = "", skipped2 = "", skipped3 = "";
		Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = dbSession.beginTransaction();

		SQLQuery query = dbSession.createSQLQuery("SELECT todoElementId, Verantwortliche_old, Beteiligte_old FROM todoelement");

		List<Object[]> tmp = query.list();

		for (Object[] x : tmp)
		{
			//migrate responsibles
			TodoElement todo = (TodoElement) dbSession.load(TodoElement.class, Integer.parseInt(x[0].toString()));

			StringTokenizer tok = new StringTokenizer((String) x[1], ",");
			while (tok.hasMoreTokens())
			{
				int empid = Integer.parseInt(tok.nextToken());

				if(empid == 0)continue;

				try
				{
					Employee emp = (Employee) dbSession.load(Employee.class, empid);
					TodoElementRespEmployee tmpElement = new TodoElementRespEmployee();
					tmpElement.setEmployee(emp);
					tmpElement.setTodoElement(todo);
					dbSession.save(tmpElement);
				} catch (HibernateException ex)
				{
					skipped1 += ", " + empid;
				}
			}

			//migrate involved
			tok = new StringTokenizer((String) x[2], ",");
			while (tok.hasMoreTokens())
			{
				int empid = Integer.parseInt(tok.nextToken());

				if(empid == 0)continue;

				try
				{
					Employee emp = (Employee) dbSession.load(Employee.class, empid);
					TodoElementInvEmployee tmpElement = new TodoElementInvEmployee();
					tmpElement.setEmployee(emp);
					tmpElement.setTodoElement(todo);
					dbSession.save(tmpElement);
				} catch (HibernateException ex)
				{
					skipped2 += ", " + empid;
				}
			}
		}

		SQLQuery query2 = dbSession.createSQLQuery("SELECT meetingId, teilnehmer_old FROM meeting");

		tmp = query2.list();

		for (Object[] x : tmp)
		{
			//migrate responsibles
			Meeting todo = (Meeting) dbSession.load(Meeting.class, Integer.parseInt(x[0].toString()));

			StringTokenizer tok = new StringTokenizer((String) x[1], ",");
			while (tok.hasMoreTokens())
			{
				int empid = Integer.parseInt(tok.nextToken());

				if(empid == 0)continue;

				try
				{
					Employee emp = (Employee) dbSession.load(Employee.class, empid);
					MeetingEmployee tmpElement = new MeetingEmployee();
					tmpElement.setEmployee(emp);
					tmpElement.setMeeting(todo);
					dbSession.save(tmpElement);
				} catch (HibernateException ex)
				{
					skipped3 += ", " + empid;
				}
			}
		}

		tx.commit();

		output.setValue("Verantwortliche:\n" + skipped1 + "\n-------\nBeteiligte:\n" + skipped2 + "\n-------\nTeilnehmer:\n" + skipped3);

		session.getAppInstance().showTrayNotification("Alle M:N Daten wurden migriert");
	}

	public void init(CurrentSession session)
	{
		this.session = session;
	}
}
