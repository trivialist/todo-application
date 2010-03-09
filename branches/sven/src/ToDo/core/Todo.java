/*
 * Todo.java
 *
 * Created on 10. Januar 2007, 05:28
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package todo.core;

import java.util.Date;
/**
 *
 * @author Marcus Hertel
 */
public class Todo {
    
    public int todoID;
    public int categoryID;
    public int meetingID;
    public int statusID;
    public int institutionID;
    public int tbz_id;
    public int areaID;
    public int topicID;
    public String area;
    public String topic;
    public String content;
    public Date reDate;
    public int reMeetType;
    public String responsible;
    public String involved;
    public String category;
    public String status;
    
    
    /** Creates a new instance of Todo */
    public Todo() {
    }
    
    public Todo(String category, int tbz_id, Date reDate, int todoID) {
        this.category = category;
        this.tbz_id = tbz_id;
        this.reDate = reDate;
        this.todoID = todoID;
    }

    public Todo(int categoryID, String category, int tbz_id, String area, String topic, Date reDate, int todoID) {
        this.categoryID = categoryID;
        this.category = category;
        this.tbz_id = tbz_id;
        this.area = area;
        this.topic = topic;
        this.reDate = reDate;
        this.todoID = todoID;
    }

    public int getTodoID() {
        return todoID;
    }
    
    public int getCategoryID() {
        return categoryID;
    }
    
    public int getMeetingID() {
        return meetingID;
    }
    
    public int getStatusID() {
        return statusID;
    }
    
    public int getInstitutionID() {
        return institutionID;
    }

    public int getTBZ_ID() {
        return tbz_id;
    }

    public int getAreaID() {
        return areaID;
    }

    public int getTopicID() {
        return topicID;
    }

    public String getArea() {
        return area;
    }

    public String getTopic() {
        return topic;
    }
    
    public String getContent() {
        return content;
    }
    
    public Date getReDate() {
        return reDate;
    }

    public int getReMeetType() {
        return reMeetType;
    }

    public String getRespons() {
        return responsible;
    }
    
    public String getOthers() {
        return involved;
    }
    
    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }
    
    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    
    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }
    
    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }
    
    public void setInstitutionID(int institutionID) {
        this.institutionID = institutionID;
    }

    public void setTBZ_ID(int tbz_id) {
        this.tbz_id = tbz_id;
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setReDate(Date reDate) {
        this.reDate = reDate;
    }

    public void setReMeetType(int reMeetType) {
        this.reMeetType = reMeetType;
    }

    public void setResponse(String responsible) {
        this.responsible = responsible;
    }
    
    public void setOthers(String involved) {
        this.involved = involved;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }
       
    public void clear() {
        todoID = 0;
        categoryID = 0;
        meetingID = 0;
        statusID = 0;
        institutionID = 0;
        tbz_id = 0;
        area = "";
        topic = "";
        content = "";
        reDate = new Date();
        responsible = "";
        involved = "";
        category = "";
        status = "";
    }
    
}