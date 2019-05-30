package com.xinyue.doudizhu.dao.model;

public class User {
	public User(){
		this.userId=1234;
		this.password="1234";
		this.username="ceshi";
		this.token = "1234";
	}
	private long userId;
	private String username;
	private String password;
	private String token;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
