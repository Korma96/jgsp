package com.mjvs.jgsp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import com.mjvs.jgsp.exceptions.UserNotFoundException;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    
    
    public User getLoggedUser() throws UserNotFoundException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
        	org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            return userRepository.findByUsername(user.getUsername());
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
        
    }
    
    public void save(User user) throws Exception {
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }


    public User getUser(String username, String password){
    	return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public boolean exists(String username)
    {
        User u = userRepository.findByUsername(username);
        return u != null;
    }
    
}
