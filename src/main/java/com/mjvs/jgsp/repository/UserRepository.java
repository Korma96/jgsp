package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {

    User save(User user);

}
