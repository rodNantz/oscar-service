package com.go.oscar.model;

public class Vote {
	
	private User user;
	private int voteFilm;
	private int voteDir;
	
	public Vote(User user, int voteFilm, int voteDir) {
		super();
		this.user = user;
		this.voteFilm = voteFilm;
		this.voteDir = voteDir;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getVoteFilm() {
		return voteFilm;
	}
	public void setVoteFilm(int voteFilm) {
		this.voteFilm = voteFilm;
	}
	public int getVoteDir() {
		return voteDir;
	}
	public void setVoteDir(int voteDir) {
		this.voteDir = voteDir;
	}
	
	
	
}
