package com.mjvs.jgsp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.repository.PassengerRepository;


@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private UserService userService;
    
    @Override
    public boolean registrate(Passenger p){
        boolean e = userService.exists(p.getUsername());

        if(e){
            return false;
        }
        
        passengerRepository.save(p);
        return true;
    }


    /*public boolean exists(String username,String password){
        Passenger p = passengerRepository.findByUsernameAndPassword(username,password);
        return  p != null;
    }*/

}