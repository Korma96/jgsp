package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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

import java.io.ByteArrayInputStream;


@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {

    private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
    private UserService userService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageModelService imageModelService;

    @Autowired
    private TicketService ticketService;


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

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/check-onetime-ticket", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> checkOnetimeTicket(@RequestParam("ticketId") Long ticketId, @RequestParam("lineId") Long lineId) throws Exception{
        String message;
        Boolean retValue;
        try {
            retValue = ticketService.checkOnetimeTicket(ticketId, lineId);
            return new ResponseEntity<>(retValue, HttpStatus.OK);

        } catch (TicketNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } catch (LineNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }

    //@PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/print-ticket", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> printTicket(@RequestParam("id") Long id) {
        ByteArrayInputStream bis = null;
        try {
            bis = ticketService.getPdfFileForTicket(id);
        } catch (TicketNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        String fileName = "myTicket.pdf";

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                //.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=citiesreport.pdf")
                // Content-Type
                .contentType(MediaType.APPLICATION_PDF)
                // Contet-Length
                //.contentLength(out.size())
                .body(new InputStreamResource(bis));
    }
}