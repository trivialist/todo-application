/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todohtmlmigrate;

/**
 *
 * @author Sven
 */
public class Main
{
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				new MigrateFrame().setVisible(true);
			}
		});
	}
}