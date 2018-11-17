package com.mjvs.jgsp.controller;


import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {


    @Autowired
    private PassengerService passengerService;


    @RequestMapping(value ="/registrate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registrate(@RequestBody PassengerDTO passengerDTO) {

        if(passengerDTO.getUsername().equals("") || passengerDTO.getPassword1().equals("") || passengerDTO.getPassword2().equals("")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(passengerDTO.getFirstName().equals("") || passengerDTO.getLastName().equals("") || passengerDTO.getEmail().equals("") || passengerDTO.getAdress().equals("")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(!passengerDTO.getPassengerType().equalsIgnoreCase("student") || !passengerDTO.getPassengerType().equalsIgnoreCase("pensioner") || !passengerDTO.getPassengerType().equalsIgnoreCase("other")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }



        if(!passengerDTO.getPassword1().equals(passengerDTO.getPassword2())){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Passenger p = new Passenger(passengerDTO.getUsername(),passengerDTO.getPassword1(),UserType.PASSENGER,UserStatus.PENDING,passengerDTO.getFirstName(),passengerDTO.getLastName(),passengerDTO.getEmail(),passengerDTO.getAdress(),PassengerType.valueOf(passengerDTO.getPassengerType()));



        boolean registrated = passengerService.registrate(p);
        if(registrated){
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
