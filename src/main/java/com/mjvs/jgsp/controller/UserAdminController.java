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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.helpers.Result;
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
	
	
	
	
	
	public static Date convertToDate(LocalDateTime dateToConvert) {
		Instant instant = dateToConvert.toInstant(ZoneOffset.UTC);
	    Date date = Date.from(instant);
	    return date;
	}
	
	    
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
	    
	    @RequestMapping(value = "/general_report", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity generalReport(@RequestBody Date startDate, Date endDate) {
	    	
	    	int monthly = 0;
	    	int daily = 0;
	    	int yearly = 0;
	    	int oneTime = 0;
	    	double profit = 0;
	    	TicketType ticketType = null;
	    	
    		if (startDate.after(endDate)) return new ResponseEntity(HttpStatus.BAD_REQUEST);
	    	
	    	List<Ticket> tickets = ticketService.getAll();
	    	
	    	
	    	for (Ticket ticket : tickets)
	    	{
	    		Date ticketDate = convertToDate(ticket.getStartDateAndTime());
	    		if (ticketDate.after(startDate) && ticketDate.before(endDate))
	    		{
	    			switch (ticketType)
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
	    public ResponseEntity lineZoneReport(@RequestParam("line_id") Long id, @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) throws Exception {
	    	
	    	int monthly = 0;
	    	int daily = 0;
	    	int yearly = 0;
	    	int oneTime = 0;
	    	double profit = 0;
    		Result<Line> lineResult;
	    	TicketType ticketType = null;

	    	
    		if (startDate.isAfter(endDate)) return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		    		
	    	List<Ticket> tickets = ticketService.getAll();
	    	
	    	
	    	for (Ticket ticket : tickets)
	    	{
	    		
	    		if (ticket.getLineZone().getId().equals(id)){
		    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
		    		
		    		if (ticketDate.isAfter(startDate) && ticketDate.isBefore(endDate))
		    		{
		    			switch (ticketType)
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
	    public ResponseEntity<ReportDTO> lineZoneDailyReport(@RequestParam("line_id") Long id, @RequestParam("requested_date") LocalDate requestedDate) throws Exception {
	    	
	    	int monthly = 0;
	    	int daily = 0;
	    	int yearly = 0;
	    	int oneTime = 0;
	    	double profit = 0;
	    	TicketType ticketType = null;
	    	

	    	
    		if (requestedDate.isAfter(LocalDate.now())) return new ResponseEntity(HttpStatus.BAD_REQUEST);
    	    		
	    	List<Ticket> tickets = ticketService.getAll();
	    	
	    	
	    	for (Ticket ticket : tickets)
	    	{
	    		
	    		if (ticket.getLineZone().getId().equals(id)){
		    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
		    		
		    		if (ticketDate.equals(requestedDate) & ticket.getTicketType() == TicketType.ONETIME)
		    		{
		    			switch (ticketType)
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
	    public ResponseEntity<ReportDTO> dailyGeneralReport(@RequestParam("requested_date") LocalDate requestedDate) {
	    	
	    	int monthly = 0;
	    	int daily = 0;
	    	int yearly = 0;
	    	int oneTime = 0;
	    	double profit = 0;
	    	TicketType ticketType = null;
	    	
    		if (requestedDate.isAfter(LocalDate.now())) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
	    	
	    	List<Ticket> tickets = ticketService.getAll();
	    	
	    	if (tickets==null) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
	    	
	    	
	    	for (Ticket ticket : tickets)
	    	{
	    		Date ticketDate = convertToDate(ticket.getStartDateAndTime());
	    		if (ticketDate.equals(requestedDate))
	    		{
	    			switch (ticketType)
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
