package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;

public class UserBackendDTO {
	
	private Long id;
 	private String username;
 	private String password;
 	private UserType userType;
 	
	public UserBackendDTO() {
	}

	public UserBackendDTO(String username, String password, UserType userType) {
		this.username = username;
		this.password = password;
		this.userType = userType;
	
	}
	
	public UserBackendDTO(Long id,String username, String password, UserType userType) {
		this.username = username;
		this.password = password;
		this.userType = userType;
	
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
 	
}
