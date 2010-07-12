/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todo.tablemodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import todo.dbcon.DB_Mitarbeiter_Connect;

/**
 *
 * @author sven
 */
public class EmployeeTreeModel implements TreeModel
{

	private Group root = new Group("root", -1);

	public EmployeeTreeModel()
	{
		DB_Mitarbeiter_Connect.openDB();
		Connection dbConnection = DB_Mitarbeiter_Connect.getCon();
		try
		{
			Statement dbStatement = dbConnection.createStatement();
			ResultSet primaryResultSet = dbStatement.executeQuery("SELECT * FROM Gruppen");

			while (primaryResultSet.next())
			{
				int groupID = primaryResultSet.getInt("GruppenID");
				Group currentGroup = new Group(primaryResultSet.getString("Name"), groupID);

				Statement memberStatement = dbConnection.createStatement();
				ResultSet secondaryResultSet = memberStatement.executeQuery("SELECT * FROM TPG INNER JOIN Stammdaten ON " +
						"TPG.PersonID = Stammdaten.Personalnummer WHERE GruppenID = " + groupID +
						" ORDER BY Nachname ASC, Vorname ASC");

				while (secondaryResultSet.next())
				{
					currentGroup.addChild(new NameLeaf(secondaryResultSet.getString("Nachname") + ", " + secondaryResultSet.getString("Vorname"), secondaryResultSet.getInt("PersonID")));
				}

				secondaryResultSet.close();
				memberStatement.close();

				root.addChild(currentGroup);
			}
			primaryResultSet.close();
			dbStatement.close();
		} catch (Exception ex)
		{
			Logger.getLogger(MeetingTableModel.class.getName()).log(Level.SEVERE, null, ex);
		}
		DB_Mitarbeiter_Connect.closeDB(dbConnection);
	}

	public static class Group
	{

		private String groupName;
		private ArrayList<Object> childElements = new ArrayList<Object>();
		private int dbId;

		public Group(String name, int id)
		{
			this.groupName = name;
			this.dbId = id;
		}

		@Override
		public String toString()
		{
			return groupName;
		}

		public void addChild(Object newChild)
		{
			childElements.add(newChild);
		}

		public ArrayList<Object> getChilds()
		{
			return childElements;
		}

		public int getId()
		{
			return dbId;
		}
	}

	public static class NameLeaf
	{

		private String leafName;
		private int dbId;

		public NameLeaf(String name, int id)
		{
			this.leafName = name;
			this.dbId = id;
		}

		public int getId()
		{
			return dbId;
		}

		@Override
		public String toString()
		{
			return leafName;
		}
	}

	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		return ((Group) parent).getChilds().get(index);
	}

	@Override
	public int getChildCount(Object parent)
	{
		return ((Group) parent).getChilds().size();
	}

	@Override
	public boolean isLeaf(Object node)
	{
		return (node instanceof NameLeaf) ? true : false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
	}
}
