package com.mjvs.jgsp.service;

import java.util.List;

import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;

public interface UserService {

	User getLoggedUser() throws UserNotFoundException;
	
    void save(User user) throws Exception;

    User getUser(String username);
    
    User getUser(String username, String password);

    boolean checkTicket(String username, String lineName) throws Exception;

    boolean exists(String username);

	List<User> getAdmins() throws UserNotFoundException;;

	boolean save(String username, String password, UserStatus userStatus, UserType userType);

	boolean deleteUser(Long id) throws UserNotFoundException;
	
	boolean acceptPassengerRequest(Long id, boolean accepted) throws UserNotFoundException;
	
	boolean adminActivation(Long id, boolean activate);
	
	boolean activatePassenger(Long id, boolean accepted);
	
	
}
