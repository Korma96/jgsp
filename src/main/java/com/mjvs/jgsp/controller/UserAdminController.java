package com.mjvs.jgsp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.dto.DeactivatedPassengerDTO;
import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.dto.RequestDTO;
import com.mjvs.jgsp.dto.UserBackendDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.dto.UserFrontendDTO;
import com.mjvs.jgsp.helpers.UserAdminHelpers;
import com.mjvs.jgsp.helpers.converter.UserConverter;
import com.mjvs.jgsp.model.ImageModel;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.service.ImageModelService;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.TicketService;
import com.mjvs.jgsp.service.UserService;
import com.mjvs.jgsp.service.ZoneService;

@RestController
@PreAuthorize("hasAuthority('USER_ADMINISTRATOR')")
@RequestMapping(value = "/userAdmin")
public class UserAdminController {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
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

	@Autowired
	private ImageModelService imageModelService;
	

	
    @RequestMapping(value = "/get-admins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserFrontendDTO>> getAdmins(){
    	List<User> users = userService.getAdmins();
    	List<UserFrontendDTO> userDtos = UserConverter.ConvertUserToUserFrontendDTOs(users);
    	return new ResponseEntity<List<UserFrontendDTO>>(userDtos, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/get-requests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RequestDTO>> getRequests(){
    	
    	List<Passenger> passengers = passengerService.getRequests();
    	List<RequestDTO> requestDtos = UserConverter.ConvertPassengerToRequestDTOs(passengers);
    	return new ResponseEntity<List<RequestDTO>>(requestDtos, HttpStatus.OK);
    }

	@RequestMapping(value = "/get-image/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id){
		ImageModel imageModel = imageModelService.getImageModel(id);

		if(imageModel != null) return new ResponseEntity<byte[]>(imageModel.getPic(), HttpStatus.OK);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
    @RequestMapping(value = "/add-admin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> add(@RequestBody UserBackendDTO userDTO) {
        try{
        	boolean retValue = userService.save(userDTO.getUsername(), userDTO.getPassword(),UserStatus.ACTIVATED,userDTO.getUserType());
        	if (retValue) return new ResponseEntity<Boolean>(retValue, HttpStatus.CREATED);
        	return new ResponseEntity<Boolean>(retValue, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

    }    
    /*
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
*/
    
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

    
    @RequestMapping(value = "/request-review/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> acceptPassengerRequest(@PathVariable("id") Long id, @RequestBody boolean accepted) {
        try{
    		boolean success = userService.acceptPassengerRequest(id, accepted);
    		return new ResponseEntity<Boolean>(success, HttpStatus.OK);
        	}
        catch (Exception e) {
        	logger.error(e.getMessage());
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/get-deactivated-passengers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeactivatedPassengerDTO>> getDeactivatedPassengers(){
    	
    	List<Passenger> passengers = passengerService.getDeactivatedPassengers();
    	List<DeactivatedPassengerDTO> deactivatedPassengerDtos = UserConverter.ConvertPassengerToDeactivatedPassengerDTO(passengers);
    	return new ResponseEntity<List<DeactivatedPassengerDTO>>(deactivatedPassengerDtos, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/activate-passenger/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> activatePassenger(@PathVariable("id") Long id, @RequestBody boolean accepted) {
        try{
    		boolean success = userService.activatePassenger(id, accepted);
    		return new ResponseEntity<Boolean>(success, HttpStatus.OK);
        	}
        catch (Exception e) {
        	logger.error(e.getMessage());
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/activation/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> adminActivation(@PathVariable("id") Long id, @RequestBody boolean activate) {
        try{
        	boolean success = userService.adminActivation(id, activate);
        	return new ResponseEntity<Boolean>(success, HttpStatus.OK);
        	}
        catch (Exception e) {
        	logger.error(e.getMessage());
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }
    /*
    @RequestMapping(value = "/decline-request", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity declinePassengerRequest(@RequestBody String username) {
        try{
        	userService.declinePassengerRequest(username);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
    */
    @RequestMapping(value = "/general-report", method = RequestMethod.GET)
    public ResponseEntity<ReportDTO> generalReport(@RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr) {
    	LocalDate startDate;
		LocalDate endDate;
		
		ReportDTO report = new ReportDTO(0, 0, 0, 0, 0);
		
		
		try {
			startDate = LocalDate.parse(UserAdminHelpers.toValidDateFormat(startDateStr));
			endDate = LocalDate.parse(UserAdminHelpers.toValidDateFormat(endDateStr));
		} catch (Exception e) {
			return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
		}
  
		if (startDate.isAfter(endDate)) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
    	
    	List<Ticket> tickets = ticketService.getAll();
    	
    	
    	for (Ticket ticket : tickets)
    	{
    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
    		if ((ticketDate.equals(startDate) || ticketDate.isAfter(startDate)) && (ticketDate.equals(endDate) || ticketDate.isBefore(endDate)) )
    		{
    			System.out.println(ticket.getTicketType());
    			report = UserAdminHelpers.calculateReport(report, ticket);
    		}
    	}
    			
        return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);	    	
    	
    }
    
    
    
    @RequestMapping(value = "/line-zone-report", method = RequestMethod.GET)
    public ResponseEntity<ReportDTO> lineZoneReport(@RequestParam("line_zone_name") String name, @RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr) throws Exception {
    	
    	LocalDate startDate;
		LocalDate endDate;
		try {
			startDate = LocalDate.parse(startDateStr);
			endDate = LocalDate.parse(endDateStr);
		} catch (Exception e) {
			return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
		}
		
		if (startDate.isAfter(endDate)) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
		
		ReportDTO report = new ReportDTO(0, 0, 0, 0, 0);
		    		
    	List<Ticket> tickets = ticketService.getAll();
    	
		List<Long> ids = new ArrayList<Long>();
    	if (zoneService.findByName(name)!=null) ids.add(zoneService.findByName(name).getId());
    	else { 
    		ids.add(lineService.findByName(name+"A").getId());
    		ids.add(lineService.findByName(name+"B").getId());

    	}
    	
    	for (Ticket ticket : tickets)
    	{
    		for (Long id: ids){
	    		if (ticket.getLineZone().getId().equals(id)) {
		    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
		    		
		    		if ((ticketDate.equals(startDate) || ticketDate.isAfter(startDate)) && (ticketDate.equals(endDate) || ticketDate.isBefore(endDate)) )
		    		{
		    			report = UserAdminHelpers.calculateReport(report, ticket);
		    		}
	    		}
    	}
    	}

        return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);	    	
    	
    }
    
    
    @RequestMapping(value = "/line_zone_daily_report", method = RequestMethod.GET)
    public ResponseEntity<ReportDTO> lineZoneDailyReport(@RequestParam("line_zone_name") String name, @RequestParam("requested_date") String requestedDateStr) throws Exception {
    	
    	LocalDate requestedDate;
		try {
			requestedDate = LocalDate.parse(UserAdminHelpers.toValidDateFormat(requestedDateStr));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    	
		ReportDTO report = new ReportDTO(0, 0, 0, 0, 0); 	
    	
		if (requestedDate.isAfter(LocalDate.now())) return new ResponseEntity(HttpStatus.BAD_REQUEST);
	    		
    	List<Ticket> tickets = ticketService.getAll();
    	
    	List<Long> ids = new ArrayList<Long>();
    	if (zoneService.findByName(name)!=null) ids.add(zoneService.findByName(name).getId());
    	else { 
    		ids.add(lineService.findByName(name+"A").getId());
    		ids.add(lineService.findByName(name+"B").getId());
    	}
    	
    	for (Ticket ticket : tickets)
    	{
    		for (Long id: ids){
	    		if (ticket.getLineZone().getId().equals(id)){
		    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
		    		if (ticketDate.equals(requestedDate)) report = UserAdminHelpers.calculateReport(report, ticket);
	    		}
    		}
    	
    	}
        return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);
    	
    }
    
    @RequestMapping(value = "/daily-general-report", method = RequestMethod.GET)
    public ResponseEntity<ReportDTO> dailyGeneralReport(@RequestParam("requestedDate") String requestedDateStr) {
    	
    	LocalDate requestedDate;
		try {
			requestedDate = LocalDate.parse(UserAdminHelpers.toValidDateFormat(requestedDateStr));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	    	ReportDTO report = new ReportDTO(0, 0, 0, 0, 0);
	    	
    		if (requestedDate.isAfter(LocalDate.now())) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
	    	
	    	List<Ticket> tickets = ticketService.getAll();
	    	
	    	if (tickets==null) return new ResponseEntity<ReportDTO>(HttpStatus.BAD_REQUEST);
	    	
	    	
	    	for (Ticket ticket : tickets)
	    	{
	    		LocalDate ticketDate = ticket.getStartDateAndTime().toLocalDate();
	    		if (ticketDate.equals(requestedDate)) report = UserAdminHelpers.calculateReport(report, ticket);
	    	}
	    	
	        return new ResponseEntity<ReportDTO>(report, HttpStatus.OK);	    	
	    }
}
