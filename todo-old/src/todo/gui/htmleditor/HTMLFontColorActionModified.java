/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.gui.htmleditor;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.StyledEditorKit;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;

public class HTMLFontColorActionModified extends HTMLTextEditAction
{
	public HTMLFontColorActionModified()
	{
		super("");
	}

	protected void sourceEditPerformed(ActionEvent e, JEditorPane editor)
	{
		if (HTMLEditor.newColor == null)
		{
			return;
		}

		String prefix = "<font color=" + HTMLUtils.colorToHex(HTMLEditor.newColor) + ">";
		String postfix = "</font>";
		String sel = editor.getSelectedText();
		if (sel == null)
		{
			editor.replaceSelection(prefix + postfix);

			int pos = editor.getCaretPosition() - postfix.length();
			if (pos >= 0)
			{
				editor.setCaretPosition(pos);
			}
		}
		else
		{
			sel = prefix + sel + postfix;
			editor.replaceSelection(sel);
		}
	}

	protected void wysiwygEditPerformed(ActionEvent e, JEditorPane editor)
	{
		if (HTMLEditor.newColor != null)
		{
			Action a = new StyledEditorKit.ForegroundAction("Color", HTMLEditor.newColor);
			a.actionPerformed(e);
		}
	}
}
