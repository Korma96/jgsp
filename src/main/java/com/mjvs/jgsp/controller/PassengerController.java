package com.mjvs.jgsp.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.dto.TicketDTO;
import com.mjvs.jgsp.model.LineZone;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.UserService;
import com.mjvs.jgsp.service.ZoneService;


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
    private LineService lineService;
    
    @Autowired
    private ZoneService zoneService;

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



        boolean registrated = passengerService.save(p);
        if(registrated){
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value ="/buy-ticket", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buyTicket(@RequestBody TicketDTO ticketDTO) {
        LocalDateTime startDateAndTime, endDateAndTime;
        LocalDateTime dateAndTime = LocalDateTime.now();
        LocalDate date;
        
        LocalTime timeThreeZero = LocalTime.of(0, 0, 0);
        LocalTime timeEndOfDay = LocalTime.of(23, 59, 59);
        LocalTime time;
        
        int dayInMonthOrMonthInYear = ticketDTO.getDayInMonthOrMonthInYear();
        
        Passenger loggedPassenger;
		try {
			User loggedUser = userService.getLoggedUser();
			loggedPassenger = (Passenger) loggedUser;
		} 
		catch(UsernameNotFoundException e) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		catch(ClassCastException e) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
        
        	
        
         switch (ticketDTO.getTicketType()) {
            case DAILY:
                int lastDayInMonth = YearMonth.of(dateAndTime.getYear(), dateAndTime.getMonthValue()).lengthOfMonth();
                if(dayInMonthOrMonthInYear < dateAndTime.getDayOfMonth() || dayInMonthOrMonthInYear > lastDayInMonth) {
                    logger.error(String.format("Day in month (%d) is less than current day or greater than count days of current month (%d)",dayInMonthOrMonthInYear, lastDayInMonth));
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                else if(dayInMonthOrMonthInYear > dateAndTime.getDayOfMonth()) time = timeThreeZero;
                else time = dateAndTime.toLocalTime();
                
                date = LocalDate.of(dateAndTime.getYear(), dateAndTime.getMonthValue(), dayInMonthOrMonthInYear);
                startDateAndTime = LocalDateTime.of(date, time);
                endDateAndTime = LocalDateTime.of(date, timeEndOfDay);
                break;
            case MONTHLY:
                int day;
                final int numberOfMonths = 12;
                if(dayInMonthOrMonthInYear < dateAndTime.getMonthValue() || dayInMonthOrMonthInYear > numberOfMonths) {
                    logger.error(String.format("Month in year (%d) is less than current month or greater than count months(12)",dayInMonthOrMonthInYear ));
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                else if(dayInMonthOrMonthInYear > dateAndTime.getMonthValue()) {
                    day = 1;
                    time = timeThreeZero;
                }
                else {
                    day = dateAndTime.getDayOfMonth();
                    time = dateAndTime.toLocalTime();
                }
                startDateAndTime = LocalDateTime.of(LocalDate.of(dateAndTime.getYear(), dayInMonthOrMonthInYear, day), time);
                day = YearMonth.of(dateAndTime.getYear(), dayInMonthOrMonthInYear).lengthOfMonth();
                endDateAndTime = LocalDateTime.of(LocalDate.of(dateAndTime.getYear(), dayInMonthOrMonthInYear, day), timeEndOfDay);
                break;
            case YEARLY:
                startDateAndTime = dateAndTime;
                int month;
                if(loggedPassenger.getPassengerType() == PassengerType.STUDENT) month = 10;
                else month = 12;
                 endDateAndTime = LocalDateTime.of(LocalDate.of(dateAndTime.getYear(), month, 31), timeEndOfDay);
                break;
            case ONETIME:
                default:
                    startDateAndTime = null;
                    endDateAndTime = null;
                    break;
        }
         boolean activated = ticketDTO.getTicketType() != TicketType.ONETIME; // only a ONETIME ticket
                                                                            // will not be activated immediately
        LineZone lineZone;
        if (ticketDTO.hasZoneNotLine()) lineZone = lineService.getLine(ticketDTO.getId());
        else lineZone = zoneService.getZone(ticketDTO.getId());
         if(lineZone == null) {
            logger.error(String.format("Line or zone with id %d does not exist!",ticketDTO.getId()));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Ticket ticket = new Ticket(startDateAndTime, endDateAndTime, ticketDTO.getTicketType() ,activated, lineZone);
        
        loggedPassenger.getTickets().add(ticket);
        passengerService.save(loggedPassenger);
        
        return new ResponseEntity(HttpStatus.CREATED);
    }
}