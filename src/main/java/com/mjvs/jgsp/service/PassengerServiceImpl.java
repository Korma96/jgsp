package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;


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


    @Override
    public void buyTicket(boolean hasZoneNotLine, Long id, int dayInMonthOrMonthInYear, TicketType ticketType)
            throws Exception {

        LocalDateTime startDateAndTime, endDateAndTime;
        LocalDateTime dateAndTime = LocalDateTime.now();
        LocalDate date;

        LocalTime timeThreeZero = LocalTime.of(0, 0, 0);
        LocalTime timeEndOfDay = LocalTime.of(23, 59, 59);
        LocalTime time;

        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

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
        boolean activated = ticketType != TicketType.ONETIME; // only a ONETIME ticket
        // will not be activated immediately
        LineZone lineZone;
        if (hasZoneNotLine) lineZone = zoneService.findById(id).getData();
        else lineZone = lineService.findById(id).getData();

        if(lineZone == null) {
            message = String.format("Line or zone with id %d does not exist!", id);
            logger.error(message);
            throw new BadRequestException(message);
        }

        Ticket ticket = new Ticket(startDateAndTime, endDateAndTime, ticketType, activated, lineZone);
        //priprema podataka
        //priceTicketService.save(new PriceTicket(LocalDate.of(2018, 11, 20), PassengerType.STUDENT, TicketType.MONTHLY, 3000, 6000, lineZone.getZone()));
        PriceTicket priceTicket = priceTicketService.getPriceTicket(loggedPassenger.getPassengerType(), ticketType,
                                                                    lineZone.getZone());
        ticket.lookAtPriceTicketAndSetPrice(priceTicket);

        loggedPassenger.getTickets().add(ticket);
        userService.save(loggedPassenger);

    }
}