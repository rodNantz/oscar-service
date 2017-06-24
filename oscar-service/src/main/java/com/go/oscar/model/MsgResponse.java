package com.go.oscar.model;

public class MsgResponse {
	
	private String action;
	private boolean status; 
	private String message;
	private String extra;		// string de objetos
	
	public MsgResponse(String action, boolean status, String message, String extra) {
		this(action, status, message);
		this.extra = extra;
	}
	
	public MsgResponse(String action, boolean status, String message) {
		super();
		this.action = action;
		this.status = status;
		this.message = message;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
}
