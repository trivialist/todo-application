/*
 * $Id$
 */

package de.konzept_e.todo.gui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import de.konzept_e.todo.core.CurrentSession;

/**
 *
 * @author sven
 */
public class AboutGui extends Panel implements GuiElement
{
	public AboutGui()
	{
		setSizeFull();

		String text = "\n\nTodoApplication ist eine Eigenentwicklung der Konzept-e für Bildung und Soziales GmbH\n\nEntwurf, Planung und Programmierung erfolgte durch:\n - Marcus Hertel\n - Sven Skrabal\n";
		TextField textField = new TextField("Über diese Anwendung:", text);
		textField.setReadOnly(true);
		textField.setRows(8);
        textField.setColumns(50);
		addComponent(textField);
		Button button = new Button("Schließen");
		button.addListener(Button.ClickEvent.class, this, "exitButtonClick");
		addComponent(button);
	}

	public void exitButtonClick(ClickEvent event)
	{
		((TabSheet)getParent()).removeComponent(this);
	}

	public void init(CurrentSession session)
	{
		//nothing here
	}
}
