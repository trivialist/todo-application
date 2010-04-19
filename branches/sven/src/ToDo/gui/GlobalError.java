/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.gui;

import javax.swing.JOptionPane;

/**
 *
 * @author sven
 */
public class GlobalError
{

	private GlobalError()
	{
		//cannot be instantiated
	}

	public static void showErrorAndExit()
	{
		JOptionPane.showMessageDialog(null, "Es trat ein interner Programfehler auf!\n\n" +
				"Die Anwendung wird nun beendet. Starten Sie diese erneut, um weiterhin " +
				"fehlerfrei arbeiten zu können.\nSollte der Fehler zum wiederholten Male auftreten, " +
				"wenden Sie sich bitte an Ihren Administrator.\n\n" +
				"Ein Fehlerprotokoll wurde erstellt.", "Interner Programfehler",
				JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
}
