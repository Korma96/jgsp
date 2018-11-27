package com.mjvs.jgsp.controller;




import com.mjvs.jgsp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.dto.TicketDTO;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageModelService imageModelService;


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
        					UserType.PASSENGER, UserStatus.PENDING,passengerDTO.getFirstName(),passengerDTO.getLastName(),
        					passengerDTO.getEmail(),passengerDTO.getAddress(),passengerDTO.getPassengerType());


        try {
            userService.save(p);
            return new ResponseEntity(HttpStatus.CREATED);

        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value = "/upload-confirmation", method = RequestMethod.POST)
    public ResponseEntity uploadConfirmation(@RequestParam("image") MultipartFile image) {
        try {
            imageModelService.save(image);

        } catch (Exception e) {
            System.out.println("Error upload image!");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/buy-ticket", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buyTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            passengerService.buyTicket(ticketDTO.hasZoneNotLine(), ticketDTO.getId(), ticketDTO.getDayInMonthOrMonthInYear(),
                    ticketDTO.getTicketType());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

}