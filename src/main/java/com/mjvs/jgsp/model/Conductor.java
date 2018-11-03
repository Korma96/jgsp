package com.mjvs.jgsp.model;

import javax.persistence.Entity;

@Entity
public class Conductor extends User {

    public Conductor(String username, String password, UserStatus userStatus) {
        super(username, password, UserType.CONDUCTOR, userStatus);
    }
}
