package com.mjvs.jgsp.dto;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;

public class UserFrontendDTO {
	
	 	private Long id;
	 	private String username;
	 	private UserType userType;
	 	private UserStatus userStatus;
	 	
	 	
	 	public UserFrontendDTO() {
			super();
		}
	 	
		public UserFrontendDTO(Long id, String username, UserType userType, UserStatus userStatus) {
			super();
			this.id = id;
			this.username = username;
			this.userType = userType;
			this.userStatus = userStatus;
		}
		
		public UserFrontendDTO(User user) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.userType = user.getUserType();
			this.userStatus = user.getUserStatus();
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
		public UserType getUserType() {
			return userType;
		}
		public void setUserType(UserType userType) {
			this.userType = userType;
		}
		public UserStatus getUserStatus() {
			return userStatus;
		}
		public void setUserStatus(UserStatus userStatus) {
			this.userStatus = userStatus;
		}
	
	    
	    

}
