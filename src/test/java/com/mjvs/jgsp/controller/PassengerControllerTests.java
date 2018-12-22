package com.mjvs.jgsp.controller;


import com.mjvs.jgsp.TestLog4j2Appender;
import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.service.LineServiceImpl;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.PassengerServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassengerControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private PassengerService passengerService;

    //private TestLog4j2Appender appender;

    private PassengerDTO passengerDTO;

    @Before
    public void setUp() {
        passengerDTO = new PassengerDTO("Slaven96","slaven","slaven","Slaven","Garic","slavengaric@gmail.com","Kralja Petra 60",PassengerType.STUDENT);
        when(passengerService.registrate(passengerDTO)).thenReturn(true);
    }

    @Test
    public void registrateTest(){
        HttpEntity<PassengerDTO> httpEntity = new HttpEntity<PassengerDTO>(passengerDTO);

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/passengers/registrate",HttpMethod.POST,httpEntity,Boolean.class);
        boolean registrated = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertTrue(registrated);
    }

    @Test
    public void Test(){
        HttpEntity<PassengerDTO> httpEntity = new HttpEntity<PassengerDTO>(passengerDTO);

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/passengers/registrate",HttpMethod.POST,httpEntity,Boolean.class);
        boolean registrated = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertTrue(registrated);
    }



}
