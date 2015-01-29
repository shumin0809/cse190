package com.cse190.petcafe;

public class UserProfileInformation 
{
	private String facebookUID;
	private String userName;
	private String firstLanguage;
	private String secondLanguage;
	private long latitude;
	private long longitude;
	private String status;
	
	
	public UserProfileInformation(String facebookUID, String userName, 
			String firstLanguage, String secondLanguage, long latitude, long longitude,
			String status)
	{
		setFacebookUID(facebookUID);
		setUserName(userName);
		setFirstLanguage(firstLanguage);
		setSecondLanguage(secondLanguage);
		setLatitude(latitude);
		setLongitude(longitude);
		setStatus(status);
	}
	
	/*
	 * SECTION: SET METHODS
	 */
	private void setFacebookUID(String facebookUID)
	{
		this.facebookUID = facebookUID;
	}
	
	private void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	private void setFirstLanguage(String firstLanguage)
	{
		this.firstLanguage = firstLanguage;
	}
	
	private void setSecondLanguage(String secondLanguage)
	{
		this.secondLanguage = secondLanguage;
	}
	
	private void setLatitude(long latitude)
	{
		this.latitude = latitude;
	}
	
	private void setLongitude(long longitude)
	{
		this.longitude = longitude;
	}
	
	private void setStatus(String status)
	{
		this.status = status;
	}
	
	/*
	 * SECTION: GET METHODS
	 */
	public String getFacebookUID()
	{
		return facebookUID;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getFirstLanguage()
	{
		return firstLanguage;
	}
	
	public String getSecondLanguage()
	{
		return secondLanguage;
	}
	
	public long getLatitude()
	{
		return latitude;
	}
	
	public long getLongitude()
	{
		return longitude;
	}
	
	public String getStatus()
	{
		return status;
	}
}
