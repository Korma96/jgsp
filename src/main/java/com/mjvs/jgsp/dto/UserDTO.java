package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.User;

public class UserDTO {

    private String username;
    private String password;

    public UserDTO() {

    }

    public UserDTO(User user) {
        this(user.getUsername(), user.getPassword());
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
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
}
