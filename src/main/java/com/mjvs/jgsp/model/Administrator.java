package com.mjvs.jgsp.model;

import javax.persistence.Entity;

@Entity
public class Administrator extends User {

    public Administrator(String username, String password) {
        super(username, password, UserType.ADMINISTRATOR, UserStatus.ACTIVATED);
    }
}
