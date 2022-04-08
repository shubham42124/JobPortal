package com.jp.entity;

public class LoginData {
	
	private String username;
	private String Email;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	
	public LoginData() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "LoginData [username=" + username + ", Email=" + Email + "]";
	}
	public LoginData(String username, String email) {
		super();
		this.username = username;
		Email = email;
	}
	
	

}
