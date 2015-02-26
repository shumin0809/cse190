package com.cse190.petcafe;

public class UserProfileInformation {
    private String facebookUID;
    private String userName;
    private String firstLanguage;
    private String secondLanguage;
    private double latitude;
    private double longitude;
    private String status;

    // private int age;
    // need space for profile picture.

    public UserProfileInformation() {
    }

    public UserProfileInformation(String facebookUID, String userName,
            String firstLanguage, String secondLanguage, double latitude,
            double longitude, String status) {
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
    public void setFacebookUID(String facebookUID) {
        this.facebookUID = facebookUID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public void setSecondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
    }

    public void setLatitude(double latitude2) {
        this.latitude = latitude2;
    }

    public void setLongitude(double longitude2) {
        this.longitude = longitude2;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*
     * SECTION: GET METHODS
     */
    public String getFacebookUID() {
        return facebookUID;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public String getSecondLanguage() {
        return secondLanguage;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }
    /*
     * public int getAge() { return age; }
     *
     * public void setAge(int age) { this.age = age; }
     */
}
