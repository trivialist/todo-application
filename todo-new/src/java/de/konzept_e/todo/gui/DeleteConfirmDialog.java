/*
 * $Id$
 */

package de.konzept_e.todo.gui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author sven
 */
public class DeleteConfirmDialog extends Window
{
	public interface DeleteAction
	{
		public void deleteEvent();
	}

	public DeleteConfirmDialog(Window parent, String title, String message, final DeleteAction deleteAction)
	{
		//add modal subwindow
		final Window subWindow = new Window(title);
		subWindow.setModal(true);
		subWindow.setWidth("40%");
		subWindow.setHeight("30%");
		subWindow.setClosable(false);

		//add text
		Label messageLabel = new Label(message);
		subWindow.addComponent(messageLabel);

		//add no button
		Button noButton = new Button("Nein", new Button.ClickListener()
		{
			// inline click-listener
			public void buttonClick(ClickEvent event)
			{
				// close the window by removing it from the parent window
				((Window) subWindow.getParent()).removeWindow(subWindow);
			}
		});

		//add yes button
		Button yesButton = new Button("Ja", new Button.ClickListener()
		{
			// inline click-listener
			public void buttonClick(ClickEvent event)
			{
				// close the window by removing it from the parent window
				((Window) subWindow.getParent()).removeWindow(subWindow);
				deleteAction.deleteEvent();
			}
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.setWidth("100%");
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(yesButton);
		buttonLayout.setComponentAlignment(yesButton, "right");
		buttonLayout.addComponent(noButton);
		subWindow.addComponent(buttonLayout);

		parent.addWindow(subWindow);
	}
}
