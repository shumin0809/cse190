package com.cse190.petcafe;

import android.app.Application;

import com.quickblox.chat.model.QBDialog;
import com.quickblox.users.model.QBUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationSingleton extends Application {

    private QBUser currentUser;

    private Map<Integer, QBUser> dialogsUsers = new HashMap<Integer, QBUser>();
    private Map<String, QBUser> friendUsers = new HashMap<String, QBUser>();

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public QBUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(QBUser currentUser) {
        this.currentUser = currentUser;
    }

    public Map<Integer, QBUser> getDialogsUsers() {
        return dialogsUsers;
    }
    
    public Map<String, QBUser> getFriendUsers()
    {
    	return friendUsers;
    }

    public void setDialogsUsers(List<QBUser> setUsers) {
        dialogsUsers.clear();

        for (QBUser user : setUsers) {
            dialogsUsers.put(user.getId(), user);
        }
    }
    
    public void setFriendUsers(List<QBUser> friendUsers)
    {
    	this.friendUsers.clear();

        for (QBUser user : friendUsers) {
        	this.friendUsers.put(user.getFacebookId(), user);
        }
    }
    
    public void addFriendUsers(List<QBUser> friendUsers)
    {
    	for (QBUser user : friendUsers) {
            this.friendUsers.put(user.getFacebookId(), user);
        }
    }

    public void addDialogsUsers(List<QBUser> newUsers) {
        for (QBUser user : newUsers) {
            dialogsUsers.put(user.getId(), user);
        }
    }

    public Integer getOpponentIDForPrivateDialog(QBDialog dialog){
        Integer opponentID = -1;
        for(Integer userID : dialog.getOccupants()){
            if(!userID.equals(getCurrentUser().getId())){
                opponentID = userID;
                break;
            }
        }
        return opponentID;
    }
}