package com.cse190.petcafe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.users.model.QBUser;

public class ApplicationSingleton extends Application {

    private static final String PROPERTY_ID = "UA-53522450-2";
    public static int GENERAL_TRACKER = 0;
    public static String GA_CATEGORY_UI = "UI Action";
    public static String GA_ACTION_BTN = "Button Clicked";

    private QBUser currentUser;

    private Map<Integer, QBUser> dialogsUsers = new HashMap<Integer, QBUser>();
    private Map<String, QBUser> friendUsers = new HashMap<String, QBUser>();

    // GoOgle Analytics Tracker
    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

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

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER)
                                ? analytics.newTracker(PROPERTY_ID)
                                : analytics.newTracker(R.xml.global_tracker);

            // setting global config by code since loading xml hangs application
            analytics.setDryRun(false);
            analytics.getLogger().setLogLevel(Logger.LogLevel.INFO);
            analytics.setLocalDispatchPeriod(30);

            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}
