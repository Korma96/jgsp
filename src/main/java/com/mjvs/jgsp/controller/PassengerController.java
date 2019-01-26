package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.*;
import com.mjvs.jgsp.helpers.converter.TicketConverter;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.TicketService;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "/passengers")
public class PassengerController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private TicketService ticketService;

    private final long MAXIMUM_ALLOWED_FILE_SIZE = 1024 * 1024; // bytes   (1Mb)


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
    @RequestMapping(value = "/change-account-type/{passengerType}", method = RequestMethod.POST/*, consumes = MediaType.APPLICATION_JSON_VALUE*/)
    public ResponseEntity uploadConfirmation(@PathVariable("passengerType") PassengerType passengerType, @RequestParam("image") MultipartFile image) {
        try {
            long imageSize = image.getSize();
            System.out.println("Image size: " + imageSize + " bytes");
            if(imageSize > MAXIMUM_ALLOWED_FILE_SIZE) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            passengerService.changeAccountType(passengerType,image);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error upload image!");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/buy-ticket", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketFrontendDTO> buyTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = passengerService.buyTicket(ticketDTO.hasZoneNotLine(), ticketDTO.getName(), ticketDTO.getDayInMonthOrMonthInYear(),
                    ticketDTO.getTicketType());
            TicketFrontendDTO ticketFrontendDTO = new TicketFrontendDTO(ticket);
            return new ResponseEntity(ticketFrontendDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/get-tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketFrontendDTO>> getTickets() {
        try {
            List<Ticket> tickets = passengerService.getTickets();
            List<TicketFrontendDTO> ticketFrontendDTOs = TicketConverter.ConvertTicketsToTicketFrontendDTOs(tickets);
            return new ResponseEntity(ticketFrontendDTOs, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/get-price", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getPriceForTicket(@RequestParam("hasZoneNotLine") boolean hasZoneNotLine,
                                                    @RequestParam("ticketType") String ticketTypeStr,
                                                       @RequestParam("zone") String zoneName) {
        try {
            TicketType ticketType = TicketType.valueOf(ticketTypeStr);
            double price = passengerService.getPrice(hasZoneNotLine, ticketType, zoneName);

            return new ResponseEntity(price, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/check-onetime-ticket", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DateTimesAndPriceDTO> checkOnetimeTicket(@RequestBody Long ticketId) throws Exception{
        try {
            DateTimesAndPriceDTO retValue = ticketService.checkOnetimeTicket(ticketId);
            if(retValue == null) return new ResponseEntity<>( HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(retValue, HttpStatus.OK);

        } catch (TicketNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        } catch (LineNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/print-ticket/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> printTicket(@PathVariable("id") Long id) {
        byte[] bytes = null;
        try {
            bytes = ticketService.getPdfFileForTicket(id);
        } catch (TicketNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(bytes, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity removeTicket(@PathVariable("id") Long id) {
        try {
            passengerService.removeTicket(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}