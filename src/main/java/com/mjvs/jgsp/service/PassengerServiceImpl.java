package com.mjvs.jgsp.service;

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
    public boolean save(Passenger p){
        boolean e = userService.exists(p.getUsername());

        if(e){
            return false;
        }
        
        passengerRepository.save(p);
        return true;
    }
    
    @Override
	public Passenger getPassenger(String username)
	{
		return passengerRepository.findByUsername(username);
	}


    /*public boolean exists(String username,String password){
        Passenger p = passengerRepository.findByUsernameAndPassword(username,password);
        return  p != null;
    }*/

}