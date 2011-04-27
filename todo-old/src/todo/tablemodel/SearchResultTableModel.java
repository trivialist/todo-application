/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package todo.tablemodel;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import todo.gui.GlobalDatabaseSearch.SearchHit;

/**
 *
 * @author Sven
 */
public class SearchResultTableModel extends AbstractTableModel
{
	private ArrayList<SearchHit> searchResults = null;
	private String[] columnNames = {"Id", "Type", "Hits", "Relevance"};

	public SearchResultTableModel(ArrayList<SearchHit> searchResults)
	{
		this.searchResults = searchResults;
	}

	@Override
	public int getRowCount()
	{
		return searchResults.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch(columnIndex)
		{
			case 0:
				return searchResults.get(rowIndex).itemId;
			case 1:
				return searchResults.get(rowIndex).itemType.getName();
			case 2:
				return reformatHits(searchResults.get(rowIndex).matchingTexts);
			case 3:
				return searchResults.get(rowIndex).matchingTexts.size();
		}

		return null;
	}

	private String reformatHits(HashMap<String, String> searchHits)
	{
		StringBuilder reformattedHits = new StringBuilder();

		for(String columnName : searchHits.keySet())
		{
			reformattedHits.append(columnName);
			reformattedHits.append(" -> ");
			reformattedHits.append(searchHits.get(columnName));
			reformattedHits.append(", ");
		}

		return reformattedHits.toString();
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}
}
