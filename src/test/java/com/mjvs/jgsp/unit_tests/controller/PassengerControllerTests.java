package com.mjvs.jgsp.unit_tests.controller;


import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.service.PassengerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;

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
        passengerDTO = new PassengerDTO("Slaven96","slaven","slaven","Slaven","Garic","slavengaric@gmail.com","Kralja Petra 60");
        when(passengerService.registrate(passengerDTO.getUsername(), passengerDTO.getPassword1(),
                passengerDTO.getPassword2(), passengerDTO.getFirstName(), passengerDTO.getLastName(), passengerDTO.getEmail(),
                passengerDTO.getAddress())).thenReturn(true);
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
