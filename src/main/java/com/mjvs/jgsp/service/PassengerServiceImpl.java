package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;


    @Override
    public boolean registrate(Passenger p){
        List<Passenger> list = passengerRepository.findAll();

        for(int i = 0;i<list.size();i++){
            if(list.get(i).getUsername().equals(p.getUsername()) || list.get(i).getEmail().equals(p.getEmail())){
                return false;
            }
        }

        passengerRepository.save(p);
        return true;
    }
}
