package com.mjvs.jgsp.repository;


import java.util.List;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.Passenger;

public interface PassengerRepository extends Repository<Passenger,Long> {

    Passenger findByUsername(String name);

    List<Passenger> findAll();



    Passenger save(Passenger passenger);

    Passenger findByUsernameAndPassword(String username, String password);
}
