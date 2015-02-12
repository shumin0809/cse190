package com.cse190.petcafe;

public class UserProfileInformation 
{
	private String facebookUID;
	private String userName;
	private String firstLanguage;
	private String secondLanguage;
	private double latitude;
	private double longitude;
	private String status;
	private int age;
	// need space for profile picture.
	
	
	public UserProfileInformation(String facebookUID, String userName, 
			String firstLanguage, String secondLanguage, double latitude, double longitude,
			String status, int age)
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
	
	private void setLatitude(double latitude2)
	{
		this.latitude = latitude2;
	}
	
	private void setLongitude(double longitude2)
	{
		this.longitude = longitude2;
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
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public String getStatus()
	{
		return status;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
