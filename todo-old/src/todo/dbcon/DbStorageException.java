/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon;

/**
 * @author Sven Skrabal
 */
public class DbStorageException extends Exception
{
	public DbStorageException()
	{
		super();
	}

	public DbStorageException(String errorDescription)
	{
		super(errorDescription);
	}
}
