package com.mjvs.jgsp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.UserService;



@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {
	
	@Autowired
    private UserService userService;
	
    @Autowired
    private PassengerService passengerService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @RequestMapping(value ="/registrate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registrate(@RequestBody PassengerDTO passengerDTO) {

    	if(passengerDTO.getUsername() == null || passengerDTO.getPassword1() == null || passengerDTO.getPassword2() == null ||
         		passengerDTO.getFirstName() == null || passengerDTO.getLastName() == null || passengerDTO.getEmail() == null 
         		|| passengerDTO.getAddress() == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    	
        if(passengerDTO.getUsername().equals("") || passengerDTO.getPassword1().equals("") || passengerDTO.getPassword2().equals("") ||
        		passengerDTO.getFirstName().equals("") || passengerDTO.getLastName().equals("") || passengerDTO.getEmail().equals("") 
        		|| passengerDTO.getAddress().equals("")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        /*if(!passengerDTO.getPassengerType().equalsIgnoreCase("student") || !passengerDTO.getPassengerType().equalsIgnoreCase("pensioner") || !passengerDTO.getPassengerType().equalsIgnoreCase("other")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }*/



        if(!passengerDTO.getPassword1().equals(passengerDTO.getPassword2())){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Passenger p = new Passenger(passengerDTO.getUsername(), passwordEncoder.encode(passengerDTO.getPassword1()),
        					UserType.PASSENGER,UserStatus.PENDING,passengerDTO.getFirstName(),passengerDTO.getLastName(),
        					passengerDTO.getEmail(),passengerDTO.getAddress(),passengerDTO.getPassengerType());



        boolean registrated = passengerService.registrate(p);
        if(registrated){
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}