package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.service.UserService;
import com.mjvs.jgsp.security.TokenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
	
	@Autowired
    TokenUtils tokenUtils;
    

    @RequestMapping(value="/login", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
    	try {
        	// Perform the authentication
        	UsernamePasswordAuthenticationToken userInfo = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(userInfo);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload user details so we can generate token
            //authentication.getDetails()
            UserDetails details = userDetailsService.loadUserByUsername(userDTO.getUsername());
            String generatedToken = tokenUtils.generateToken(details);
            Map<String,String> result = new HashMap<>();
            result.put("token",generatedToken);
            return new ResponseEntity< Map<String,String>>(result,HttpStatus.OK);
            //return new ResponseEntity<String>(generatedToken, HttpStatus.OK);
        }
    	catch (Exception e) {
            return new ResponseEntity<String>("Invalid login", HttpStatus.BAD_REQUEST);
        }
    	
    }

    @PreAuthorize("hasAuthority('CONTROLLOR')")
    @RequestMapping(value ="/checkticket/{username}/{lineName}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> checkPassengerTicket(@PathVariable String username,@PathVariable String lineName) {
        boolean valid;
        try{
            valid = userService.checkTicket(username,lineName);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Boolean>(valid,HttpStatus.OK);
    }


    public long computeSubtractTwoDateTime(LocalDateTime ldt1, LocalDateTime ldt2) {
        long sub = ChronoUnit.SECONDS.between(ldt1, ldt2);
        return sub;
    }



}
