package com.cse190.petcafe;

public class CommentInformation {

	private String facebookUID;
	private int post_id;
	private int comment_id;
	private String body;
	public String getFacebookUID() {
		return facebookUID;
	}
	public void setFacebookUID(String facebookUID) {
		this.facebookUID = facebookUID;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public CommentInformation(String facebookUID, int post_id, int comment_id,
			String body) {
		super();
		this.facebookUID = facebookUID;
		this.post_id = post_id;
		this.comment_id = comment_id;
		this.body = body;
	}
	
}
