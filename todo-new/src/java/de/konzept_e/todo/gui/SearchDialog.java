/*
 * $Id$
 */

package de.konzept_e.todo.gui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author sven
 */
public class SearchDialog extends Window
{
	public interface SearchAction
	{
		public void searchEvent(String searchPhrase);
	}

	public SearchDialog(Window parent, String title, String message, final SearchAction searchAction)
	{
				//add modal subwindow
		final Window subWindow = new Window(title);
		subWindow.setModal(true);
		subWindow.setWidth("20%");
		subWindow.setHeight("32%");
		subWindow.setClosable(false);

		//add text
		Label messageLabel = new Label(message);
		subWindow.addComponent(messageLabel);

		//add search field
		final TextField searchField = new TextField();
		searchField.setColumns(17);
		subWindow.addComponent(searchField);

		//add no button
		Button cancelButton = new Button("Abbrechen", new Button.ClickListener()
		{
			// inline click-listener
			public void buttonClick(ClickEvent event)
			{
				// close the window by removing it from the parent window
				((Window) subWindow.getParent()).removeWindow(subWindow);
			}
		});

		//add yes button
		Button searchButton = new Button("Suchen", new Button.ClickListener()
		{
			// inline click-listener
			public void buttonClick(ClickEvent event)
			{
				// close the window by removing it from the parent window
				((Window) subWindow.getParent()).removeWindow(subWindow);
				searchAction.searchEvent((String)searchField.getValue());
			}
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.setWidth("100%");
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(searchButton);
		buttonLayout.setComponentAlignment(searchButton, "right");
		buttonLayout.addComponent(cancelButton);
		subWindow.addComponent(buttonLayout);

		parent.addWindow(subWindow);
	}
}
