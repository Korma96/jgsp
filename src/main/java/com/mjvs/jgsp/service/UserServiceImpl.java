package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.repository.UserRepository;
import com.mjvs.jgsp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) throws Exception {
        userRepository.save(user);
    }
}
