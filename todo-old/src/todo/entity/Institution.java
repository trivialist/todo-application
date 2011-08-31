/**
 * This file is part of 'Todo Application'
 * 
 * @see			http://www.konzept-e.de/
 * @copyright	2006-2011 Konzept-e für Bildung und Soziales GmbH
 * @author		Marcus Hertel, Sven Skrabal
 * @license		LGPL - http://www.gnu.org/licenses/lgpl.html
 * 
 */

package todo.entity;

public class Institution
{
	private String institution;
	private int institutionID;

	/** Creates a new instance of Institution */
	public Institution(String institution)
	{
		this.institution = institution;
	}

	public Institution(int institutionID, String institution)
	{
		this.institutionID = institutionID;
		this.institution = institution;
	}

	public int getInstitutionID()
	{
		return institutionID;
	}

	public String getInstitution()
	{
		return institution;
	}

	public void setInstitutionID(int institutionID)
	{
		this.institutionID = institutionID;
	}

	public void setInstitution(String institution)
	{
		this.institution = institution;
	}
}
