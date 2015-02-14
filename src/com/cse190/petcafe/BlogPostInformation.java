package com.cse190.petcafe;

public class BlogPostInformation {
	private String facebookId;
	private String title;
	private String type;
	private String body;
	private int rating;
	
	public BlogPostInformation(String facebookId, String title, String type, String body, int rating)
	{
		setFacebookId(facebookId);
		setTitle(title);
		setType(type);
		setBody(body);
		setRating(rating);
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
}
