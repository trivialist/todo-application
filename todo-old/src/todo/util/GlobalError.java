/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */
package todo.util;

import javax.swing.JOptionPane;

public class GlobalError
{
	private GlobalError()
	{
		//cannot be instantiated
	}

	public static void showErrorAndExit()
	{
		JOptionPane.showMessageDialog(null, "Es trat ein interner Programfehler auf!\n\n"
											+ "Die Anwendung wird nun beendet. Starten Sie diese erneut, um weiterhin "
											+ "fehlerfrei arbeiten zu können.\nSollte der Fehler zum wiederholten Male auftreten, "
											+ "wenden Sie sich bitte an Ihren Administrator.\n\n"
											+ "Ein Fehlerprotokoll wurde erstellt.", "Interner Programfehler",
									  JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
}
