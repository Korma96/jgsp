package com.mjvs.jgsp.integration_tests.service;

import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.service.*;
import com.mjvs.jgsp.repository.PriceTicketRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.*;

import  static com.mjvs.jgsp.JgspApplicationTests.prepareLoggedUser;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//port je definisan u test/resources/application.properties
public class PassengerServiceTest {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private PriceTicketRepository priceTicketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;


    @Transactional
    @Rollback(true)
    @Test
    public void buyTicketTestSuccessWithLine() {
        Zone zone = new Zone("osma_zona", TransportType.BUS);
        Line line = new Line("888A", zone, 45);
        zone.addLine(line);
        zone = zoneRepository.save(zone);
        line = zone.getLines().get(0); // ovo radimo da bismo u line imali id koji mu je jpa dodelio

        final TicketType ticketType = TicketType.MONTHLY;
        PriceTicket priceTicket = new PriceTicket(LocalDate.of(2018, 12, 1), PassengerType.OTHER, ticketType, 2000, 4000, zone);
        priceTicketRepository.save(priceTicket);
        prepareLoggedUser(userService, passwordEncoder, authenticationManager);

        Ticket ticket = null;
        try {
            ticket = passengerService.buyTicket(false, line.getName().substring(0,line.getName().length()-1), 12, ticketType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(ticket);
        assertEquals(ticket.getLineZone(), line);
        assertEquals(ticketType, ticket.getTicketType());
        assertEquals(priceTicket.getPriceLine(), ticket.getPrice(), 0.001);

        Passenger passenger = null;
        try {
            passenger = (Passenger) userService.getLoggedUser();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(passenger.getPassengerType(), ticket.getPassengerType());

        boolean retValue = passenger.containsTicket(ticket);
        assertTrue(retValue);
    }

    @Transactional
    @Rollback(true)
    @Test
    public void buyTicketTestSuccessWithZone() {
        Zone zone = new Zone("osma_zona", TransportType.BUS);

        final TicketType ticketType = TicketType.MONTHLY;
        PriceTicket priceTicket = new PriceTicket(LocalDate.of(2018, 12, 1), PassengerType.OTHER, ticketType, 2000, 4000, zone);
        priceTicketRepository.save(priceTicket);
        prepareLoggedUser(userService, passwordEncoder, authenticationManager);

        Ticket ticket = null;
        try {
            ticket = passengerService.buyTicket(true, zone.getName(), 12, ticketType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(ticket);
        assertEquals(ticket.getLineZone(), zone);
        assertEquals(ticketType, ticket.getTicketType());
        assertEquals(priceTicket.getPriceZone(), ticket.getPrice(), 0.001);

        Passenger passenger = null;
        try {
            passenger = (Passenger) userService.getLoggedUser();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(passenger.getPassengerType(), ticket.getPassengerType());

        boolean retValue = passenger.containsTicket(ticket);
        assertTrue(retValue);
    }

    @Transactional
    @Rollback(true)
    @Test(expected = BadRequestException.class)
    public void getLineZoneTestThrowsBadRequestException() throws Exception {
        passengerService.getLineZone(true, "tamo_neka_leva_zona", TicketType.ONETIME);
    }

    @Transactional
    @Rollback(true)
    @Test(expected = Exception.class)
    public void getLineZoneTestThrowsExceptionForLine() throws Exception {
        Zone zone = new Zone("osma_zona", TransportType.BUS);
        Line line = new Line("888A", zone, 45);
        zone.addLine(line);
        zone = zoneRepository.save(zone);
        line = zone.getLines().get(0); // ovo radimo da bismo u line imali id koji mu je jpa dodelio

        passengerService.getLineZone(true, line.getName().substring(0,line.getName().length()-1), TicketType.DAILY);
    }

    @Transactional
    @Rollback(true)
    @Test(expected = Exception.class)
    public void getLineZoneTestThrowsExceptionForZone() throws Exception {
        Zone zone = new Zone("osma_zona", TransportType.BUS);
        zone = zoneRepository.save(zone);

        passengerService.getLineZone(false, zone.getName(), TicketType.DAILY);
    }

    @Transactional
    @Rollback(true)
    @Test
    public void getLineZoneTestSuccessForZone() throws Exception {
        Zone zone = new Zone("osma_zona", TransportType.BUS);
        zone = zoneRepository.save(zone);

        LineZone retZone = passengerService.getLineZone(true, zone.getName(), TicketType.DAILY);
        assertEquals(zone, retZone);
    }

    @Transactional
    @Rollback(true)
    @Test
    public void getLineZoneTestSuccessForLine() throws Exception {
        Zone z = new Zone("sedma_zona", TransportType.BUS);
        Line l = new Line("777A", z,45);
        z.addLine(l);
        z = zoneRepository.save(z);
        l = z.getLines().get(0); // ovo radimo da bismo u line imali id koji mu je jpa dodelio

        LineZone line =passengerService.getLineZone(false, l.getName().substring(0, l.getName().length()-1), TicketType.DAILY);
        assertEquals(l, line);
    }

    @Test
    public void createStartAndEndDateTimeTestSuccessONETIME() {
        LocalDateTime[] retValue = new LocalDateTime[0];
        try {
            retValue = passengerService.createStartAndEndDateTime(TicketType.ONETIME, PassengerType.OTHER,-1);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }

        assertNull(retValue[0]);
        assertNull(retValue[1]);
    }

    @Test
    public void createStartAndEndDateTimeTestSuccessDAILY() {
        LocalDateTime[] retValue = null;
        LocalDateTime dateAndTime = LocalDateTime.now();
        int day = dateAndTime.getDayOfMonth();

        try {
            retValue = passengerService.createStartAndEndDateTime(TicketType.DAILY, PassengerType.OTHER, day);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }

        LocalTime timeStart;
        if(day > dateAndTime.getDayOfMonth()) timeStart = LocalTime.of(0, 0, 0);
        else timeStart = dateAndTime.toLocalTime();

        LocalDate date = LocalDate.of(dateAndTime.getYear(), dateAndTime.getMonthValue(), day);
        LocalDateTime expectedStartDateAndTime = LocalDateTime.of(date, timeStart);
        LocalDateTime expectedEndDateAndTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        Assertions.assertThat(retValue[0]).isCloseTo(expectedStartDateAndTime, within(500, ChronoUnit.MILLIS));
        Assertions.assertThat(retValue[1]).isCloseTo(expectedEndDateAndTime, within(500, ChronoUnit.MILLIS));
    }

    @Test
    public void createStartAndEndDateTimeTestSuccessMONTHLY_OTHER() {
        LocalDateTime[] retValue = null;
        LocalDateTime dateAndTime = LocalDateTime.now();
        int month = dateAndTime.getMonthValue();

        try {
            retValue = passengerService.createStartAndEndDateTime(TicketType.MONTHLY, PassengerType.OTHER, month);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }

        LocalTime timeStart;
        int day;

        if(month > dateAndTime.getMonthValue()) {
            day = 1;
            timeStart = LocalTime.of(0, 0, 0);
        }
        else {
            day = dateAndTime.getDayOfMonth();
            timeStart = dateAndTime.toLocalTime();
        }

        LocalDate date = LocalDate.of(dateAndTime.getYear(), month, day);
        LocalDateTime expectedStartDateAndTime = LocalDateTime.of(date, timeStart);
        day = YearMonth.of(dateAndTime.getYear(), month).lengthOfMonth();
        LocalDateTime expectedEndDateAndTime = LocalDateTime.of(LocalDate.of(dateAndTime.getYear(), month, day),
                LocalTime.of(23, 59, 59));

        Assertions.assertThat(retValue[0]).isCloseTo(expectedStartDateAndTime, within(500, ChronoUnit.MILLIS));
        Assertions.assertThat(retValue[1]).isCloseTo(expectedEndDateAndTime, within(500, ChronoUnit.MILLIS));
    }

    @Test
    public void createStartAndEndDateTimeTestSuccessYEARLY_STUDENT() {
        LocalDateTime[] retValue = null;
        LocalDateTime dateAndTime = LocalDateTime.now();
        int month = 10;

        try {
            retValue = passengerService.createStartAndEndDateTime(TicketType.YEARLY, PassengerType.STUDENT, -1);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }

        LocalDateTime expectedStartDateAndTime = dateAndTime;

        LocalDate date = LocalDate.of(dateAndTime.getYear(), month, 31);
        LocalDateTime expectedEndDateAndTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        Assertions.assertThat(retValue[0]).isCloseTo(expectedStartDateAndTime, within(500, ChronoUnit.MILLIS));
        Assertions.assertThat(retValue[1]).isCloseTo(expectedEndDateAndTime, within(500, ChronoUnit.MILLIS));
    }

    @Test
    public void createStartAndEndDateTimeTestSuccessYEARLY_PENSIONER() {
        LocalDateTime[] retValue = null;
        LocalDateTime dateAndTime = LocalDateTime.now();
        int month = 12;

        try {
            retValue = passengerService.createStartAndEndDateTime(TicketType.YEARLY, PassengerType.PENSIONER, -1);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }

        LocalDateTime expectedStartDateAndTime = dateAndTime;

        LocalDate date = LocalDate.of(dateAndTime.getYear(), month, 31);
        LocalDateTime expectedEndDateAndTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        Assertions.assertThat(retValue[0]).isCloseTo(expectedStartDateAndTime, within(500, ChronoUnit.MILLIS));
        Assertions.assertThat(retValue[1]).isCloseTo(expectedEndDateAndTime, within(500, ChronoUnit.MILLIS));
    }

    @Transactional
    @Rollback(true)
    @Test(expected = BadRequestException.class)
    public void createStartAndEndDateTimeTestThrowsBadRequestExceptionForInvalidDay() throws BadRequestException {
        passengerService.createStartAndEndDateTime(TicketType.DAILY, PassengerType.OTHER, 32);
    }

    @Transactional
    @Rollback(true)
    @Test(expected = BadRequestException.class)
    public void createStartAndEndDateTimeTestThrowsBadRequestExceptionForInvalidMonth() throws BadRequestException {
        passengerService.createStartAndEndDateTime(TicketType.MONTHLY, PassengerType.OTHER, 13);
    }

}