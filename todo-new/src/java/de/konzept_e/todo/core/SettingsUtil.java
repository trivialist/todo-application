/**
 * $Id$
 */
package de.konzept_e.todo.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for global settings
 *
 * @author Sven Skrabal
 */
public class SettingsUtil
{
	private static final Properties applicationProperties = new Properties();

	/**
	 * Constructor cannot be used
	 */
	private SettingsUtil()
	{
		//nothing here
	}

	/**
	 * Load settings from a given XML file
	 * 
	 * @param filename	File to load properties from
	 * @throws IOException In case of any errors this exception will be thrown
	 */
	public static void loadFromXML(File filename) throws IOException
	{
		try
		{
			FileInputStream inputStream = new FileInputStream(filename);
			applicationProperties.loadFromXML(inputStream);
			inputStream.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(SettingsUtil.class.getName()).log(Level.SEVERE, null, ex);
			throw new IOException(ex);
		}
	}

	/**
	 * Store settings to given/new XML file
	 * 
	 * @param filename	File where settings shoud be stored in
	 * @throws IOException	In case of any errors this exception wil be thrown
	 */
	public static void storeToXML(File filename) throws IOException
	{
		try
		{
			FileOutputStream outputStream = new FileOutputStream(filename);
			applicationProperties.storeToXML(outputStream, "TodoApplication-Settings");
			outputStream.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(SettingsUtil.class.getName()).log(Level.SEVERE, null, ex);
			throw new IOException(ex);
		}
	}

	/**
	 * Get the property value for the given key
	 * 
	 * @param key	Key identifier for setting value
	 * @return	The value for the given identifier. Empty string if property wasnt set before
	 */
	public static String getProperty(String key)
	{
		return applicationProperties.getProperty(key, "");
	}

	/**
	 * Set/update a given property value
	 *
	 * @param key	Key identifier for setting
	 * @param value	Value to set
	 */
	public static void setProperty(String key, String value)
	{
		applicationProperties.setProperty(key, value);
	}
}
