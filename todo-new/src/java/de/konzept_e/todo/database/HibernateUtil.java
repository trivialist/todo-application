/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.database;

import de.konzept_e.todo.database.entities.Area;
import de.konzept_e.todo.database.entities.AreaTopic;
import de.konzept_e.todo.database.entities.Category;
import de.konzept_e.todo.database.entities.Employee;
import de.konzept_e.todo.database.entities.TodoElementInvEmployee;
import de.konzept_e.todo.database.entities.TodoElementRespEmployee;
import de.konzept_e.todo.database.entities.Institution;
import de.konzept_e.todo.database.entities.Meeting;
import de.konzept_e.todo.database.entities.MeetingEmployee;
import de.konzept_e.todo.database.entities.MeetingType;
import de.konzept_e.todo.database.entities.Memo;
import de.konzept_e.todo.database.entities.Status;
import de.konzept_e.todo.database.entities.TodoElement;
import de.konzept_e.todo.database.entities.Topic;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author sven
 */
public class HibernateUtil
{
	private static final SessionFactory sessionFactory;

	static
	{
		try
		{
			AnnotationConfiguration config = new AnnotationConfiguration();
			
            config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/todo");
            config.setProperty("hibernate.connection.username", "root");
            config.setProperty("hibernate.connection.password", "root");
            config.setProperty("hibernate.connection.pool_size", "1");
            config.setProperty("hibernate.connection.autocommit", "true");
            config.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
            config.setProperty("hibernate.show_sql", "true");
            config.setProperty("hibernate.format_sql", "true");
            config.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
            config.setProperty("hibernate.current_session_context_class", "thread");
            config.setProperty("hibernate.bytecode.provider", "javassist");

			config.addAnnotatedClass(Area.class);
			config.addAnnotatedClass(Category.class);
			config.addAnnotatedClass(Employee.class);
			config.addAnnotatedClass(Institution.class);
			config.addAnnotatedClass(Meeting.class);
			config.addAnnotatedClass(MeetingType.class);
			config.addAnnotatedClass(Memo.class);
			config.addAnnotatedClass(Status.class);
			config.addAnnotatedClass(TodoElement.class);
			config.addAnnotatedClass(Topic.class);
			config.addAnnotatedClass(TodoElementRespEmployee.class);
			config.addAnnotatedClass(TodoElementInvEmployee.class);
			config.addAnnotatedClass(MeetingEmployee.class);
			config.addAnnotatedClass(AreaTopic.class);

			sessionFactory = config.buildSessionFactory();
		}
		catch (Throwable ex)
		{
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
}
