package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;


    @Override
    public boolean registrate(Passenger p){
        List<Passenger> list = passengerRepository.findAll();


        boolean e = exists(p.getUsername(),p.getPassword());

        if(e){
            return false;
        }
        passengerRepository.save(p);
        return true;
    }


    public boolean exists(String username,String password){
        Passenger p = passengerRepository.findByUsernameAndPassword(username,password);
        return  p != null;
    }




}
