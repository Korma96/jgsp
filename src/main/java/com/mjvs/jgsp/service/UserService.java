package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.User;

public interface UserService {

    void save(User user) throws Exception;

    User getUser(String username);
}
