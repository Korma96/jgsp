package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Administrator;

public class AdministratorDTO {

    private String username;
    private String password;

    public AdministratorDTO() {

    }

    public AdministratorDTO(Administrator administrator) {
        this(administrator.getUsername(), administrator.getPassword());
    }

    public AdministratorDTO(String username, String password) {
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
