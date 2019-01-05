package com.mjvs.jgsp.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.User;

public interface UserRepository extends Repository<User, Long> {

    User save(User user);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username,String password);
    List<User> findByDeleted(boolean deleted);
    User findByIdAndDeleted(Long id, boolean deleted);
    User findByUsernameAndDeleted(String username, boolean deleted);

}
