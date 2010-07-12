/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Sven Skrabal
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface DbId
{
	public String name();
}
