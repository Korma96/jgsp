package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) throws Exception {
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }


    public User tryToLogin(UserDTO userDTO){
        boolean e = exists(userDTO.getUsername(),userDTO.getPassword());

        User u = null;
        if(e){
            u = userRepository.findByUsername(userDTO.getUsername());
        }
        return u;
    }

    @Override
    public boolean exists(String username,String password)
    {
        User u = userRepository.findByUsernameAndPassword(username,password);
        return u != null;
    }
}
