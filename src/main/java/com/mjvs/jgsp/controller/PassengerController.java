package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.service.ImageModelService;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.TicketService;
import com.mjvs.jgsp.service.UserService;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
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

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;


@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ImageModelService imageModelService;

    @Autowired
    private TicketService ticketService;


    @RequestMapping(value ="/registrate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> registrate(@RequestBody PassengerDTO passengerDTO) {

        boolean registrated = passengerService.registrate(passengerDTO.getUsername(), passengerDTO.getPassword1(),
                passengerDTO.getPassword2(), passengerDTO.getFirstName(), passengerDTO.getLastName(), passengerDTO.getEmail(),
                passengerDTO.getAddress());

        if(registrated){
            return new ResponseEntity<Boolean>(registrated,HttpStatus.CREATED);
        }
        return new ResponseEntity<Boolean>(registrated,HttpStatus.BAD_REQUEST);
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