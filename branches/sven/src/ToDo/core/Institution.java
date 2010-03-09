/*
 * Institution.java
 *
 * Created on 29. Dezember 2006, 18:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.core;

/**
 *
 * @author Marcus Hertel
 */
public class Institution {
    
    private String institution;
    private int institutionID;
    /** Creates a new instance of Institution */
    public Institution(String institution) {
        this.institution = institution;
    }
    
    public Institution(int institutionID, String institution) {
        this.institutionID = institutionID;
        this.institution = institution;
    }
    
    public int getInstitutionID() {
        return institutionID;
    }
    
    public String getInstitution() {
        return institution;
    }
    
    public void setInstitutionID(int institutionID) {
        this.institutionID = institutionID;
    }
    
    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
