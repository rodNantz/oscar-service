package com.go.oscar.model;

public class User {

	private long codU;
	private String user;
	private String pass;
	private String token;
	private boolean voted;
	
	public User(long codU, String user, String pass, String token, boolean voted){
		this.setCodU(codU);
		this.setUser(user);
		this.setPass(pass);
		this.setToken(token);
		this.setVoted(voted);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String residuo) {
		this.pass = residuo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getCodU() {
		return codU;
	}

	public void setCodU(long codU) {
		this.codU = codU;
	}

	public boolean isVoted() {
		return voted;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}
	
}
