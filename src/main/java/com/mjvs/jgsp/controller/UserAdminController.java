package com.mjvs.jgsp.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.dto.UserBackendDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.dto.UserFrontendDTO;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.converter.UserConverter;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.TicketService;
import com.mjvs.jgsp.service.UserService;
import com.mjvs.jgsp.service.ZoneService;

@RestController
@RequestMapping(value = "/userAdmin")
public class UserAdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PassengerService passengerService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private ZoneService zoneService;
	

	
    @RequestMapping(value = "/get-admins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserFrontendDTO>> getAdmins(){
    	List<User> users = userService.getAdmins();
    	List<UserFrontendDTO> userDtos = UserConverter.ConvertUserToUserFrontendDTOs(users);
    	return new ResponseEntity<List<UserFrontendDTO>>(userDtos, HttpStatus.OK);
    }
	
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> add(@RequestBody UserBackendDTO userDTO) {
        try{
        	boolean retValue = userService.save(userDTO.getUsername(), userDTO.getPassword(),UserStatus.ACTIVATED,userDTO.getUserType());
        	return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

    }    
    
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
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
    
    
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try{
        	userService.deleteUser(id);
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
        	if (passenger!=null && passenger.getUserStatus().equals(UserStatus.PENDING)){
        		if (passengerType.equals("student"))
        		{
        			passenger.setPassengerType(PassengerType.STUDENT);
        		}
        		else if (passengerType.equals("pensioner"))
        		{
        			passenger.setPassengerType(PassengerType.PENSIONER);
        		} else {
					passenger.setPassengerType(PassengerType.OTHER);
        		}

        		passenger.setUserStatus(UserStatus.ACTIVATED);
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
    
    @RequestMapping(value = "/general_report", method = RequestMethod.GET)
    public ResponseEntity generalReport(@RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr) {
    	LocalDate startDate;
		LocalDate endDate;
		try {
			startDate = LocalDate.parse(startDateStr);
			endDate = LocalDate.parse(endDateStr);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    	
    	int monthly = 0;
    	int daily = 0;
    	int yearly = 0;
    	int oneTime = 0;
    	double profit = 0;	    	
    	
		if (startDate.isAfter(endDate)) return new ResponseEntity(HttpStatus.BAD_REQUEST);
    	
    	List<Ticket> tickets = ticketService.getAll();
    	
    	
    	for (Ticket ticket : tickets)
    	{
    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
    		if (ticketDate.isAfter(startDate) && ticketDate.isBefore(endDate))
    		{
    			switch (ticket.getTicketType())
    			{
    			case DAILY:
    				daily++;
    			case MONTHLY:
    				monthly++;
    			case YEARLY:
    				yearly++;
    			case ONETIME:
    				oneTime++;
    			}
    			profit += ticket.getPrice();
    			
    		}
    		
    	}
    	ReportDTO report = new ReportDTO(oneTime, daily, monthly, yearly, profit);
        return new ResponseEntity(report, HttpStatus.OK);	    	
    	
    }
    
    
    
    @RequestMapping(value = "/line_zone_report", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity lineZoneReport(@RequestParam("line_id") Long id, @RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr) throws Exception {
    	
    	LocalDate startDate;
		LocalDate endDate;
		try {
			startDate = LocalDate.parse(startDateStr);
			endDate = LocalDate.parse(endDateStr);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
    	int monthly = 0;
    	int daily = 0;
    	int yearly = 0;
    	int oneTime = 0;
    	double profit = 0;
    	
		if (startDate.isAfter(endDate)) return new ResponseEntity(HttpStatus.BAD_REQUEST);
		    		
    	List<Ticket> tickets = ticketService.getAll();
    	
    	
    	for (Ticket ticket : tickets)
    	{
    		
    		if (ticket.getLineZone().getId().equals(id)){
	    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
	    		
	    		if (ticketDate.isAfter(startDate) && ticketDate.isBefore(endDate))
	    		{
	    			switch (ticket.getTicketType())
	    			{
	    			case DAILY:
	    				daily++;
	    			case MONTHLY:
	    				monthly++;
	    			case YEARLY:
	    				yearly++;
	    			case ONETIME:
	    				oneTime++;
	    			}
	    			profit += ticket.getPrice();
	    			
	    		}
    	}
    	}
    	ReportDTO report = new ReportDTO(oneTime, daily, monthly, yearly, profit);
        return new ResponseEntity(report, HttpStatus.OK);	    	
    	
    }
    
    
    @RequestMapping(value = "/line_zone_daily_report", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> lineZoneDailyReport(@RequestParam("line_id") Long id, @RequestParam("requested_date") String requestedDateStr) throws Exception {
    	
    	LocalDate requestedDate;
		try {
			requestedDate = LocalDate.parse(requestedDateStr);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    	
    	
    	int monthly = 0;
    	int daily = 0;
    	int yearly = 0;
    	int oneTime = 0;
    	double profit = 0;
    	

    	
		if (requestedDate.isAfter(LocalDate.now())) return new ResponseEntity(HttpStatus.BAD_REQUEST);
	    		
    	List<Ticket> tickets = ticketService.getAll();
    	
    	
    	for (Ticket ticket : tickets)
    	{
    		
    		if (ticket.getLineZone().getId().equals(id)){
	    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
	    		
	    		if (ticketDate.equals(requestedDate))
	    		{
	    			switch (ticket.getTicketType())
	    			{
	    			case DAILY:
	    				daily++;
	    			case MONTHLY:
	    				monthly++;
	    			case YEARLY:
	    				yearly++;
	    			case ONETIME:
	    				oneTime++;
	    			}
	    			profit += ticket.getPrice();
	    			
	    		}
    		}
    	
    	}
    	ReportDTO report = new ReportDTO(oneTime, daily, monthly, yearly, profit);
        return new ResponseEntity(report, HttpStatus.OK);
    	
    }
    
    @RequestMapping(value = "/daily_general_report", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> dailyGeneralReport(@RequestParam("startDate") String requestedDateStr) {
    	
    	LocalDate requestedDate;
		try {
			requestedDate = LocalDate.parse(requestedDateStr);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	    	int monthly = 0;
	    	int daily = 0;
	    	int yearly = 0;
	    	int oneTime = 0;
	    	double profit = 0;
	    	
    		if (requestedDate.isAfter(LocalDate.now())) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
	    	
	    	List<Ticket> tickets = ticketService.getAll();
	    	
	    	if (tickets==null) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
	    	
	    	
	    	for (Ticket ticket : tickets)
	    	{
	    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
	    		
	    		if (ticketDate.equals(requestedDate))
	    		{
	    			switch (ticket.getTicketType())
	    			{
	    			case DAILY:
	    				daily++;
	    			case MONTHLY:
	    				monthly++;
	    			case YEARLY:
	    				yearly++;
	    			case ONETIME:
	    				oneTime++;
	    			}
	    			profit += ticket.getPrice();
	    			
	    		}
	    		
	    	}
	    	ReportDTO report = new ReportDTO(oneTime, daily, monthly, yearly, profit);
	        return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);	    	
	    	
	    }
}
