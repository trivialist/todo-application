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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelationResolver
{
	public static boolean relationItemExists(Connection openConnection, String destinationTable, String destinationColumn, int destinationId, int personnelId)
	{
		try
		{
			Statement statement = openConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT " + destinationColumn + " FROM " + destinationTable + " WHERE " + destinationColumn + " = " + destinationId + " AND personnelID = " + personnelId);

			//try to access item -> if this throws an exception it doesnt exist
			resultSet.next();
			resultSet.getInt("destinationColumn");

			resultSet.close();
			statement.close();

			return true;
		}
		catch (Exception ex)
		{
			//do nothing just return
			return false;
		}
	}

	public static void insertRelation(Connection openConnection, String destinationTable, String destinationColumn, int personnelId, int destinationId)
	{
		try
		{
			Statement statement = openConnection.createStatement();
			statement.execute("INSERT INTO " + destinationTable + " (personnelID, " + destinationColumn + ") VALUES (" + personnelId + ", " + destinationId + ")");
			statement.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(RelationResolver.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
	}

	public static void removeRelation(Connection openConnection, String destinationTable, String destinationColumn, int personnelId, int destinationId)
	{
		try
		{
			Statement statement = openConnection.createStatement();
			statement.execute("DELETE FROM " + destinationTable + " WHERE " + destinationColumn + " = " + destinationId + " AND personnelID = " + personnelId);
			statement.close();
		}
		catch (Exception ex)
		{
			Logger.getLogger(RelationResolver.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
	}

	public static void updateRelations(Connection openConnection, String destinationTable, String destinationColumn, ArrayList<Integer> personnelIds, int destinationId)
	{
		try
		{
			Statement statement = openConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT personnelID FROM " + destinationTable + " WHERE " + destinationColumn + " = " + destinationId);

			//get all existing relations
			ArrayList<Integer> existingPersonnelIds = new ArrayList<Integer>();
			while (resultSet.next())
			{
				existingPersonnelIds.add(resultSet.getInt("personnelID"));
			}

			resultSet.close();
			statement.close();

			//find new relations
			for (int currentRelation : personnelIds)
			{
				if (!existingPersonnelIds.contains(currentRelation))
				{
					insertRelation(openConnection, destinationTable, destinationColumn, currentRelation, destinationId);
				}
			}

			//find removed relations
			for (int currentRelation : existingPersonnelIds)
			{
				if (!personnelIds.contains(currentRelation))
				{
					removeRelation(openConnection, destinationTable, destinationColumn, currentRelation, destinationId);
				}
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(RelationResolver.class.getName()).log(Level.SEVERE, null, ex);
			GlobalError.showErrorAndExit();
		}
	}
}
