package com.mjvs.jgsp.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;

    @Column(name = "username", unique = true, nullable = false)
    protected String username;

    @Column(name = "password", unique = false, nullable = false)
    protected String password;

    @Column(name = "user_type", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    protected UserType userType;

    @Column(name = "user_status", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    protected UserStatus userStatus;

    protected User() {

    }

    public User(String username, String password, UserType userType, UserStatus userStatus) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public Long getId() { return id; }

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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
