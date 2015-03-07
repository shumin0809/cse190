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
	//private int age;
	// need space for profile picture.
	private String email;
	private String phoneNumber;
	
	public UserProfileInformation(String facebookUID) {
		setFacebookUID("");
		setUserName("");
		setFirstLanguage("");
		setSecondLanguage("");
		setLatitude(0.0);
		setLongitude(0.0);
		setStatus("");
		setEmail("");
		setPhoneNumber("");
		setFacebookUID(facebookUID);
	}
	
	public UserProfileInformation(String facebookUID, String userName, 
			String firstLanguage, String secondLanguage, double latitude, double longitude,
			String status)
	{
		setFacebookUID(facebookUID);
		setUserName(userName);
		setFirstLanguage(firstLanguage);
		setSecondLanguage(secondLanguage);
		setLatitude(latitude);
		setLongitude(longitude);
		setStatus(status);
		setEmail("");
		setPhoneNumber("");
	}
	
	public UserProfileInformation(String facebookUID, String userName, 
			String firstLanguage, String secondLanguage, double latitude, double longitude,
			String status, String email, String phoneNumber)
	{
		setFacebookUID(facebookUID);
		setUserName(userName);
		setFirstLanguage(firstLanguage);
		setSecondLanguage(secondLanguage);
		setLatitude(latitude);
		setLongitude(longitude);
		setStatus(status);
		setEmail(email);
		setPhoneNumber(phoneNumber);
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
/*
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}*/

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
