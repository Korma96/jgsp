package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.User;

public interface UserService {

    void save(User user) throws Exception;

    User getUser(String username);

    User tryToLogin(UserDTO userDTO);


    boolean exists(String username,String password);
}
