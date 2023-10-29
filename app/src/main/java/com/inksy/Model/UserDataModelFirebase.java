package com.inksy.Model;

public class UserDataModelFirebase {

    boolean isActive;
    Object lastSeen;
    boolean isOnChat;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Object getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Object lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean getIsOnChat() {
        return isOnChat;
    }

    public void setIsOnChat(boolean isOnChat) {
        this.isOnChat = isOnChat;
    }

}
