package com.mjvs.jgsp.service;

import com.mjvs.jgsp.exceptions.UserNotFoundException;
import com.mjvs.jgsp.model.User;

public interface UserService {

	User getLoggedUser() throws UserNotFoundException;
	
    void save(User user) throws Exception;

    User getUser(String username);
    
    User getUser(String username, String password);

    boolean exists(String username);
    
}
