package com.xinyue.doudizhu.command;

public class LoginResponse extends AbstractGameCommand{

	private long userId;
	
	private String token;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
