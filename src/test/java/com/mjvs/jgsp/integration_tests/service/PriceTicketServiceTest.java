package com.mjvs.jgsp.integration_tests.service;

import com.mjvs.jgsp.helpers.exception.PriceTicketNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.service.*;
import com.mjvs.jgsp.repository.PriceTicketRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriceTicketServiceTest {

    @Autowired
    private PriceTicketService priceTicketService;

    @Autowired
    private PriceTicketRepository priceTicketRepository;

    @Autowired
    private ZoneRepository zoneRepository;


    @Transactional(readOnly = false)
    @Rollback(true)
    @Test
    public void getPriceTicketTestSuccess() {
        PriceTicket priceTicket1 = new PriceTicket(LocalDate.now(), PassengerType.OTHER, TicketType.MONTHLY,
                1500, 3500, new Zone("sedma zona", TransportType.BUS));
        priceTicketRepository.save(priceTicket1);

        PriceTicket priceTicket = null;
        try {
            priceTicket = priceTicketService.getLatestPriceTicket(priceTicket1.getPassengerType(), priceTicket1.getTicketType(), priceTicket1.getZone());
        } catch (PriceTicketNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(priceTicket);
        assertEquals(priceTicket1.getDateFrom(), priceTicket.getDateFrom());
        assertEquals(priceTicket1.getPriceLine(), priceTicket.getPriceLine(), 0);
        assertEquals(priceTicket1.getPriceZone(), priceTicket.getPriceZone(), 0);
    }

    @Transactional(readOnly = false)
    @Rollback(true)
    @Test
    public void getPriceTicketTestSuccess2() {
        Zone zone = zoneRepository.save(new Zone("sedma zona", TransportType.BUS));

        PriceTicket priceTicket1 = new PriceTicket(LocalDate.of(2018, 10, 1), PassengerType.OTHER, TicketType.MONTHLY,
                1500, 3500, zone);
        PriceTicket priceTicket2 = new PriceTicket(LocalDate.of(2018, 11, 1), PassengerType.OTHER, TicketType.MONTHLY,
                1500, 3500, zone);
        PriceTicket priceTicket3 = new PriceTicket(LocalDate.of(2017, 12, 1), PassengerType.OTHER, TicketType.MONTHLY,
                1500, 3500, zone);
        priceTicketRepository.save(priceTicket1);
        priceTicketRepository.save(priceTicket2);
        priceTicketRepository.save(priceTicket3);

        PriceTicket priceTicket = null;
        try {
            priceTicket = priceTicketService.getLatestPriceTicket(priceTicket1.getPassengerType(), priceTicket1.getTicketType(), priceTicket1.getZone());
        } catch (PriceTicketNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(priceTicket);
        assertEquals(priceTicket2.getDateFrom(), priceTicket.getDateFrom());
        assertEquals(priceTicket2.getPriceLine(), priceTicket.getPriceLine(), 0);
        assertEquals(priceTicket2.getPriceZone(), priceTicket.getPriceZone(), 0);
    }

    @Transactional(readOnly = false)
    @Rollback(true)
    @Test(expected = PriceTicketNotFoundException.class)
    public void getPriceTicketTestThrowsPriceTicketNotFoundException1() throws PriceTicketNotFoundException {
        Zone zone = zoneRepository.save(new Zone("sedma zona", TransportType.BUS));
        priceTicketService.getLatestPriceTicket(PassengerType.OTHER, TicketType.MONTHLY,  zone);
    }

}