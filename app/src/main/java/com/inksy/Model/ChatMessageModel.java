package com.inksy.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ChatMessageModel implements Serializable {

    String message;
    Long senderId;
    String senderName;
    public Object timestamp;
    Long type;
    String userImage;
    int journalID;
//    boolean isUnRead;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getJournalID() {
        return journalID;
    }

    public void setJournalID(int journalID) {
        this.journalID = journalID;
    }

/*
    public boolean getIsUnRead() {
        return isUnRead;
    }

    public void setIsUnRead(boolean isUnRead) {
        this.isUnRead = isUnRead;
    }
*/
}
