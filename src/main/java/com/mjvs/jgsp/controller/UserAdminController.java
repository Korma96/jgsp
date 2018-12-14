package com.mjvs.jgsp.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.UserService;

@RestController
@RequestMapping(value = "/userAdmin")
public class UserAdminController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PassengerService passengerService;
	
	    
	    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity update(@RequestBody UserDTO userDTO) {
	        try{
	        	User user = userService.getUser(userDTO.getUsername());
	        	user.setPassword(userDTO.getPassword());
	        	userService.save(user);
	        }
	        catch (Exception e) {
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity(HttpStatus.OK);
	    }
	    
	    
	    @RequestMapping(value = "/delete", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity delete(@RequestBody String username) {
	        try{
	        	User user = userService.getUser(username);
	        	user.setUserStatus(UserStatus.DEACTIVATED);
	        	userService.save(user);
	        }
	        catch (Exception e) {
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	            
	        }

	        return new ResponseEntity(HttpStatus.OK);
	    }

	    
	    
	    @RequestMapping(value = "/accept_request", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity acceptPassengerRequest(@RequestBody String username, String passengerType) {
	        try{
	        	Passenger passenger = passengerService.getPassenger(username);
	        	User userAdmin = userService.getLoggedUser();
	        	if (passenger!=null){
	        		if (passengerType.equals("student"))
	        		{
	        			passenger.setPassengerType(PassengerType.STUDENT);
	        		}
	        		else if (passengerType.equals("pensioner"))
	        		{
	        			passenger.setPassengerType(PassengerType.PENSIONER);
	        		}
	        		
	        		passenger.setVerifiedBy(userAdmin);
	        		userService.save(passenger);
	        	}
	        }
	        catch (Exception e) {
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity(HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "/decline_request", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity declinePassengerRequest(@RequestBody String username) {
	        try{
	        	Passenger passenger = passengerService.getPassenger(username);
	        	if (passenger!=null){
	        		passenger.setIdConfirmation(null);
	        		userService.save(passenger);
	        	}
	        }
	        catch (Exception e) {
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity(HttpStatus.OK);
	    }
	    
	    
	    

}
