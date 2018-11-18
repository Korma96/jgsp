package com.mjvs.jgsp.repository;


import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PassengerRepository extends Repository<Passenger,Long> {

    Passenger findByUsername(String name);

    List<Passenger> findAll();



    Passenger save(Passenger passenger);

    Passenger findByUsernameAndPassword(String username, String password);
}
