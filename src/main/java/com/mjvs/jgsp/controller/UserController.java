package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.service.UserService;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
	
	@Autowired
    TokenUtils tokenUtils;
    

    @RequestMapping(value="/login", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
    	try {
        	// Perform the authentication
        	UsernamePasswordAuthenticationToken userInfo = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(userInfo);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload user details so we can generate token
            //authentication.getDetails()
            UserDetails details = userDetailsService.loadUserByUsername(userDTO.getUsername());
            String generatedToken = tokenUtils.generateToken(details);
            return new ResponseEntity<String>(generatedToken, HttpStatus.OK);
        } 
    	catch (Exception e) {
            return new ResponseEntity<String>("Invalid login", HttpStatus.BAD_REQUEST);
        }
    	
    }

    @PreAuthorize("hasAuthority('CONTROLLOR')")
    @RequestMapping(value ="/checkticket/{username}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> checkPassengerTicket(@PathVariable String username) {
        boolean valid;
        try{
            valid = userService.checkTicket(username);
        }catch (Exception e){
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Boolean>(valid,HttpStatus.OK);
    }


    public long computeSubtractTwoDateTime(LocalDateTime ldt1, LocalDateTime ldt2) {
        long sub = ChronoUnit.SECONDS.between(ldt1, ldt2);
        return sub;
    }



}
