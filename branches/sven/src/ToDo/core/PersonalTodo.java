package todo.core;
/*
 * PersonalTodo.java
 *
 * Created on 4. November 2007, 00:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Marcus Hertel
 */
public class PersonalTodo {
    
    private String type;
    private boolean involv = false;
    private boolean respons = false;
    private int todoID;
    private int categoryID;
    private int areaID;
    private int institutionID;
    private int statusID;
    private String topic;
    private String content;
    private String reDate;
    private String responsible;
    private String involved;
    private int meetingID;
    private String meetPlace;
    private String meetDate;
    private String meetName;
    
    /** Creates a new instance of PersonalTodo */
    public PersonalTodo() {
    }
    
    /** GET - Methoden */
    
    public String getType() {
        return type;
    }
    
    public boolean getInvolv() {
        return involv;
    }
    
    public boolean getRespons() {
        return respons;
    } 
    
    public int getTodoID() {
        return todoID;
    }
    
    public int getCategoryID() {
        return categoryID;
    }
    
    public int getAreaID() {
        return areaID;
    }
    
    public int getInstitutionID() {
        return institutionID;
    }
    
    public int getStatusID() {
        return statusID;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getReDate() {
        return reDate;
    }
    
    public String getResponsible() {
        return responsible;
    }
    
    public String getInvolved() {
        return involved;
    }
    
     public int getMeetingID() {
        return meetingID;
    }
    
    public String getMeetPlace() {
        return meetPlace;
    }
    
    public String getMeetDate() {
        return meetDate;
    }
    
    public String getMeetName() {
        return meetName;
    }
    
    /** SET - Methoden */
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setInvolv(boolean involv) {
        this.involv = involv;
    }
    
    public void setResponse(boolean respons) {
        this.respons = respons;
    }
    
    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    
    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }
    
    public void setInstitutionID(int institutionID) {
        this.institutionID = institutionID;
    }
    
    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setReDate(String reDate) {
        this.reDate = reDate;
    }
    
    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }
    
    public void setInvolved(String involved) {
        this.involved = involved;
    }
    
    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }
    
    public void setMeetPlace(String meetPlace) {
        this.meetPlace = meetPlace;
    }
    
    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }
    
    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }
}
