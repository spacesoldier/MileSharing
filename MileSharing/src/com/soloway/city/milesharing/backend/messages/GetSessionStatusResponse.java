package com.soloway.city.milesharing.backend.messages;

public class GetSessionStatusResponse {
	private int sessionStatus;
	private int statusCode;
	private String errorMessage;
	public int getSessionStatus() {
		return sessionStatus;
	}
	public void setSessionStatus(int sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
