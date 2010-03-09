/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ToDo.core;

import java.sql.Timestamp;
/**
 *
 * @author Marcus Hertel
 */
public class Memo {

    public int memoID;
    public int todoID;
    public String comment;
    public Timestamp memoTimestamp;
    public String winUser;

    public Memo() {
    }

    public Memo(int todoID, String comment) {
        this.todoID = todoID;
        this.comment = comment;
    }

    public int getMemoID() {
        return memoID;
    }

    public int getTodoID() {
        return todoID;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getMemoTimestamp() {
        return memoTimestamp;
    }

    public String getWinUser() {
        return winUser;
    }

    public void setMemoID(int memoID) {
        this.memoID = memoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMemoTimestamp(Timestamp memoTimestamp) {
        this.memoTimestamp = memoTimestamp;
    }

    public void setWinUser(String winUser) {
        this.winUser = winUser;
    }

}
