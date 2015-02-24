package com.cse190.petcafe;

public class FriendInformation {
	private String friendFacebookID;
	private String friendName;
	private String friendStatus;
	
	public FriendInformation(String friendFacebookID, String friendName, String friendStatus)
	{
		this.setFriendFacebookID(friendFacebookID);
		this.setFriendName(friendName);
		this.setFriendStatus(friendStatus);
	}

	public String getFriendFacebookID() {
		return friendFacebookID;
	}

	public void setFriendFacebookID(String friendFacebookID) {
		this.friendFacebookID = friendFacebookID;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	
	public String getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(String friendStatus) {
		this.friendStatus = friendStatus;
	}
}
