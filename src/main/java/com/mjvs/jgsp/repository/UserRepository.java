package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    User findByUsername(String username);

}
