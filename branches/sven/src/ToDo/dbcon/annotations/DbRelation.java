/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.dbcon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Sven Skrabal
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface DbRelation
{
	public enum RELATION_TYPE {ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE, MANY_TO_MANY};
	public RELATION_TYPE type();
	public boolean autoload() default false;
}