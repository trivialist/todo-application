/*
 * MeetingType.java
 *
 * Created on 28. Dezember 2006, 00:20
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
public class MeetingType {
    
    private String meetingType;
    private int meetingTypeID;
    
    /** Creates a new instance of MeetingType */
    public MeetingType() {
    }
    
    public MeetingType(String meetingType) {
        this.meetingType = meetingType;
    }
    
    public MeetingType(int meetingTypeID, String meetingType) {
        this.meetingTypeID = meetingTypeID;
        this.meetingType = meetingType;
    }
    
    public String getMeetingType() {
        return meetingType;
    } 
    
    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }
    
    public int getMeetingTypeID() {
        return meetingTypeID;
    }
    
    public void setMeetingTypeID(int meetingTypeID) {
        this.meetingTypeID = meetingTypeID;
    }
}
