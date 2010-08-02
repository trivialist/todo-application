/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.konzept_e.todo.gui;

import com.vaadin.ui.Component;
import de.konzept_e.todo.core.CurrentSession;

/**
 *
 * @author sven
 */
public interface GuiElement extends Component
{
	public void init(CurrentSession session);
}
