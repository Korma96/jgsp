package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.*;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.PassengerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PassengerServiceImpl implements PassengerService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private LineService lineService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private PriceTicketService priceTicketService;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ImageModelService imageModelService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Ticket buyTicket(boolean hasZoneNotLine, String lineZoneName, int dayInMonthOrMonthInYear, TicketType ticketType)
            throws Exception {

        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

        String message;

        LineZone lineZone = getLineZone(hasZoneNotLine, lineZoneName, ticketType);

        LocalDateTime startDateAndTime = null;
        LocalDateTime endDateAndTime = null;
        if(ticketType != TicketType.ONETIME) {
            LocalDateTime[] retDateTimes = createStartAndEndDateTime(ticketType, loggedPassenger.getPassengerType(), dayInMonthOrMonthInYear);
            startDateAndTime = retDateTimes[0];
            endDateAndTime = retDateTimes[1];
        }

        Ticket ticket = new Ticket(startDateAndTime, endDateAndTime, ticketType, loggedPassenger.getPassengerType(),
                                    /*activated,*/ lineZone);
        //priprema podataka
        //priceTicketService.save(new PriceTicket(LocalDate.of(2018, 11, 20), PassengerType.STUDENT, TicketType.MONTHLY, 3000, 6000, lineZone.getZone()));
        //priceTicketService.save(new PriceTicket(LocalDate.of(2018, 11, 20), PassengerType.PENSIONER, TicketType.MONTHLY, 3100, 6100, lineZone.getZone()));
        //priceTicketService.save(new PriceTicket(LocalDate.of(2018, 11, 20), PassengerType.OTHER, TicketType.MONTHLY, 3500, 6500, lineZone.getZone()));

        if(lineZone != null) {
            PriceTicket priceTicket = priceTicketService.getLatestPriceTicket(loggedPassenger.getPassengerType(), ticketType, lineZone.getZone());
            ticket.lookAtPriceTicketAndSetPrice(priceTicket);
        }

        ticket = ticketService.save(ticket); // moglo je i beze ovoga, ali posto hocemo da ova metoda vrati
                                            // ticket koji je sacuvan u bazi, onda nam je neophodno
        loggedPassenger.getTickets().add(ticket);
        userService.save(loggedPassenger);

        return ticket;
    }

    @Override
    public boolean registrate(String username, String password1, String password2, String firstName,
                              String lastName, String email, String address) {
        if(username == null || password1 == null || password2 == null || firstName == null || lastName == null
                || email == null || address == null){
            return false;
        }

        if(username.equals("") || password1.equals("") || password2.equals("") || firstName.equals("")
                || lastName.equals("") || email.equals("") || address.equals("")){
            return false;
        }

        /*if(!passengerDTO.getPassengerType().equalsIgnoreCase("student") || !passengerDTO.getPassengerType().equalsIgnoreCase("pensioner") || !passengerDTO.getPassengerType().equalsIgnoreCase("other")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }*/

        if(userService.exists(username)) return false;

        if(!password1.equals(password2)){
            return false;
        }

        Passenger p = new Passenger(username, passwordEncoder.encode(password1),
                UserType.PASSENGER, UserStatus.ACTIVATED,firstName,lastName,
                email,address,PassengerType.OTHER);

        try{
            passengerRepository.save(p);
            return true;
        }catch(Exception e){
            e.printStackTrace();

        }

        return  false;
    }

    public LineZone getLineZone(boolean hasZoneNotLine, String lineZoneName, TicketType ticketType) throws Exception {
        LineZone lineZone;
        String message;

        if(hasZoneNotLine) {
            if(ticketType == TicketType.ONETIME) {
                message = "A one-time ticket can not be issued for a zone!";
                logger.error(message);
                throw new BadRequestException(message);
            }

            lineZone = zoneService.findByName(lineZoneName);
        }
        else {
            final String sufix = "A";
            lineZone = lineService.findByName(lineZoneName+sufix);
        }

        if(lineZone == null) {
            if(hasZoneNotLine) message = "Zone";
            else message = "Line";

                message += String.format(" with name %s does not exist!", lineZoneName);
                logger.error(message);

            if(hasZoneNotLine) throw new ZoneNotFoundException(message);
            else throw new LineNotFoundException(message);
        }

        return lineZone;
    }

    public LocalDateTime[] createStartAndEndDateTime(TicketType ticketType, PassengerType passengerType, int dayInMonthOrMonthInYear) throws BadRequestException {
        LocalDateTime startDateAndTime, endDateAndTime;
        LocalDateTime dateAndTime = LocalDateTime.now();
        LocalDate date;

        LocalTime timeThreeZero = LocalTime.of(0, 0, 0);
        LocalTime timeEndOfDay = LocalTime.of(23, 59, 59);
        LocalTime time;

        String message;

        switch (ticketType) {
            case DAILY:
                int lastDayInMonth = YearMonth.of(dateAndTime.getYear(), dateAndTime.getMonthValue()).lengthOfMonth();
                if(dayInMonthOrMonthInYear < dateAndTime.getDayOfMonth() || dayInMonthOrMonthInYear > lastDayInMonth) {
                    message = String.format("Day in month (%d) is less than current day or greater than count days of current month (%d)",dayInMonthOrMonthInYear, lastDayInMonth);
                    logger.error(message);
                    throw new BadRequestException(message);
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
                    message = String.format("Month in year (%d) is less than current month or greater than count months(12)",dayInMonthOrMonthInYear );
                    logger.error(message);
                    throw  new BadRequestException(message);
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
                if(passengerType == PassengerType.STUDENT) month = 10;
                else month = 12;

                endDateAndTime = LocalDateTime.of(LocalDate.of(dateAndTime.getYear(), month, 31), timeEndOfDay);
                break;
            case ONETIME:
            default:
                startDateAndTime = null;
                endDateAndTime = null;
                break;
        }

        return new LocalDateTime[] {startDateAndTime, endDateAndTime};
    }

    @Override
    public List<Ticket> getTickets() throws UserNotFoundException {
        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

        /*loggedPassenger.getTickets().sort(new Comparator<Ticket>() {
            @Override
            public int compare(Ticket o1, Ticket o2) {
                if(o1.getStartDateAndTime() == null && o2.getStartDateAndTime() != null) return -1;
                else if (o1.getStartDateAndTime() != null && o2.getStartDateAndTime() == null) return 1;
                else if(o1.getStartDateAndTime() == null && o2.getStartDateAndTime() == null) return 0;

                return o1.getStartDateAndTime().compareTo(o2.getStartDateAndTime());
            }
        });*/

       return loggedPassenger.getTickets();
    }

    @Override
    public double getPrice(boolean hasZoneNotLine, TicketType ticketType, String zoneName) throws UserNotFoundException, ZoneNotFoundException, PriceTicketNotFoundException {
        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

        Zone zone = zoneService.findByName(zoneName);
        if(zone == null) {
            String message = String.format("Zone with name %s not found!", zoneName);
            logger.error(message);
            throw new ZoneNotFoundException(message);
        }

        PriceTicket priceTicket = priceTicketService.getLatestPriceTicket(loggedPassenger.getPassengerType(), ticketType, zone);

        if(hasZoneNotLine) return priceTicket.getPriceZone();
        return priceTicket.getPriceLine();
    }

    @Override
    public void changeAccountType(PassengerType newPassengerType, MultipartFile image) throws Exception {
        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;
        loggedPassenger.setNewPassengerType(newPassengerType);
        passengerRepository.save(loggedPassenger);

        imageModelService.save(image);
    }

    @Override
    public void removeTicket(Long id) throws UserNotFoundException, TicketNotFoundException, CanNotBeDeletedException {
        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

        Ticket ticket;
        int indexForRemove = -1;
        boolean ticketNotFound = true;
        LocalDateTime currentDateTime;

        List<Ticket> tickets = loggedPassenger.getTickets();

        for(int i = 0; i < tickets.size(); i++) {
            ticket = tickets.get(i);

            if(ticket.getId().longValue() == id.longValue()) {
                ticketNotFound = false;

                if(ticket.getStartDateAndTime() == null || ticket.getEndDateAndTime() == null) {
                    indexForRemove = i;
                }
                else {
                    currentDateTime = LocalDateTime.now();
                    // 10 sec lufta
                    if(ticket.getStartDateAndTime().isAfter(currentDateTime.minusSeconds(10))
                            || currentDateTime.isAfter(ticket.getEndDateAndTime())) {
                        indexForRemove = i;
                    }
                }

                break;
            }
        }

        if(ticketNotFound) throw new TicketNotFoundException();
        else {
            if(indexForRemove == -1) throw new CanNotBeDeletedException();
        }

        ticket = loggedPassenger.getTickets().get(indexForRemove);
        ticket.setDeleted(true);
        ticketService.save(ticket);

        loggedPassenger.getTickets().remove(indexForRemove);
        passengerRepository.save(loggedPassenger);

    }

    @Override
	public Passenger getPassenger(String username)
	{
		return passengerRepository.findByUsername(username);
	}

	@Override
	public Passenger save(Passenger p) {
		return passengerRepository.save(p);
	}
	
	@Override
	public List<Passenger> getAll(){
		return passengerRepository.findAll();
	}
	
	@Override
	public List<Passenger> getRequests(){
		List<Passenger> passengers = passengerRepository.findAll();
		
		List<Passenger> requests = passengers.stream()
				.filter(p -> p.getNewPassengerType() != null)
				.collect(Collectors.toList());
		
		return requests;
	}

	@Override
	public List<Passenger> getDeactivatedPassengers() {
		List<Passenger> passengers = passengerRepository.findAll();
		
		List<Passenger> deactivatedPassengers = passengers.stream()
				.filter(p -> p.getUserStatus() != UserStatus.ACTIVATED)
				.collect(Collectors.toList());
		
		return deactivatedPassengers;
	}
	 
	
}



    /*public boolean exists(String username,String password){
        Passenger p = passengerRepository.findByUsernameAndPassword(username,password);
        return  p != null;
    }*/

