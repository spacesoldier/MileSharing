package com.soloway.city.milesharing.api;

public class UserSession {
	private String SessionId;
	private String TokenId;
	private String UserId;
	
	private boolean isOnline;
	
	public UserSession(){
		SessionId = "nondefined";
		TokenId = "nondefined";
		UserId = "nondefined";
		isOnline = false;
	}
	
	public String getSessionId() {
		return SessionId;
	}
	public void setSessionId(String sessionId) {
		this.SessionId = sessionId;
	}
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}
	
	public String getTokenId() {
		return TokenId;
	}
	public void setTokenId(String TokenId) {
		TokenId = TokenId;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
}
