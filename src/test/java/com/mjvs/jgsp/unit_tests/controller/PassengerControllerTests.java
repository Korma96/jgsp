package com.mjvs.jgsp.unit_tests.controller;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.dto.TicketDTO;
import com.mjvs.jgsp.dto.TicketFrontendDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.helpers.exception.PriceTicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.helpers.exception.ZoneNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.security.TokenUtils;
import com.mjvs.jgsp.service.PassengerService;
import com.mjvs.jgsp.service.UserDetailsServiceImpl;
import com.mjvs.jgsp.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Ignore
public class PassengerControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private PassengerService passengerService;


    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    TokenUtils tokenUtils;

    private String accessToken1;

    private UserDetails userDetails1;


    //private TestLog4j2Appender appender;

    private PassengerDTO passengerDTO;

    private PassengerType newPassengerTypeSuccess;
    private MockMultipartFile imageSucces;
    private PassengerType newPassengerTypeFail;
    private MockMultipartFile imageFail;

    private TicketDTO ticketDtoSuccess;
    private TicketDTO ticketDtoFail;
    private Ticket ticketSuccess;
    private TicketFrontendDTO ticketFrontendDtoSuccess;

    private double priceSuccess;
    private boolean hasZoneNotLineSuccess;
    private boolean hasZoneNotLineFail1;
    private boolean hasZoneNotLineFail2;
    private boolean hasZoneNotLineFail3;
    private TicketType ticketTypeSuccess;
    private TicketType ticketTypeFail1;
    private TicketType ticketTypeFail2;
    private TicketType ticketTypeFail3;
    private String zoneSuccess;
    private String zoneFail1;
    private String zoneFail2;
    private String zoneFail3;

    @Before
    public void setUp() throws Exception {
        List<GrantedAuthority> garantedAuthorities = AuthorityUtils.createAuthorityList(UserType.PASSENGER.toString());
        userDetails1 = new org.springframework.security.core.userdetails.User("svetislav", "svetislav", garantedAuthorities);
        when(userDetailsService.loadUserByUsername(userDetails1.getUsername())).thenReturn(userDetails1);
        accessToken1 = "token1";
        when(tokenUtils.generateToken(userDetails1)).thenReturn(accessToken1);
        when(tokenUtils.getUsernameFromToken(accessToken1)).thenReturn(userDetails1.getUsername());
        when(tokenUtils.validateToken(accessToken1, userDetails1)).thenReturn(true);


        passengerDTO = new PassengerDTO("Slaven96","slaven","slaven","Slaven","Garic","slavengaric@gmail.com","Kralja Petra 60");
        when(passengerService.registrate(passengerDTO.getUsername(), passengerDTO.getPassword1(),
                passengerDTO.getPassword2(), passengerDTO.getFirstName(), passengerDTO.getLastName(), passengerDTO.getEmail(),
                passengerDTO.getAddress())).thenReturn(true);

        newPassengerTypeSuccess = PassengerType.STUDENT;
        String imageRelativePath = "/images/confirmation.jpg";
        File file = new File(URLDecoder.decode(getClass().getResource(imageRelativePath).getFile(),"UTF-8"));
        imageSucces = new MockMultipartFile("picture.png", new FileInputStream(file));
        Mockito.doThrow(Exception.class).when(passengerService).changeAccountType(newPassengerTypeSuccess, imageSucces);
        Mockito.doNothing().when(passengerService).changeAccountType(newPassengerTypeFail, imageFail);

        ticketDtoSuccess = new TicketDTO(false, "blbla", 2, TicketType.ONETIME);
        ticketSuccess = new Ticket(12L, LocalDateTime.now(), LocalDateTime.now(), ticketDtoSuccess.getTicketType(),  PassengerType.STUDENT,2000.0, new Zone(45L, "neka_zona"));
        ticketFrontendDtoSuccess = new TicketFrontendDTO(ticketSuccess);
        when(passengerService.buyTicket(ticketDtoSuccess.hasZoneNotLine(), ticketDtoSuccess.getName(), ticketDtoSuccess.getDayInMonthOrMonthInYear(), ticketDtoSuccess.getTicketType())).thenReturn(ticketSuccess);

        ticketDtoFail = new TicketDTO(false, "wqqwqw", 3, TicketType.DAILY);
        when(passengerService.buyTicket(ticketDtoFail.hasZoneNotLine(), ticketDtoFail.getName(), ticketDtoFail.getDayInMonthOrMonthInYear(), ticketDtoFail.getTicketType())).thenThrow(Exception.class);

        hasZoneNotLineSuccess = true;
        hasZoneNotLineFail1 = false;
        hasZoneNotLineFail2 = false;
        hasZoneNotLineFail3 = false;
        ticketTypeSuccess= TicketType.MONTHLY;
        ticketTypeFail1 = TicketType.ONETIME;
        ticketTypeFail2 = TicketType.DAILY;
        ticketTypeFail3 = TicketType.YEARLY;
        zoneSuccess = "neka_tamo_leva_zona";
        zoneFail1 = "neka_tamo_desna_zona1";
        zoneFail2 = "neka_tamo_desna_zona2";
        zoneFail3 = "neka_tamo_desna_zona3";
        priceSuccess = 2000.0;
        when(passengerService.getPrice(hasZoneNotLineSuccess, ticketTypeSuccess, zoneSuccess)).thenReturn(priceSuccess);
        when(passengerService.getPrice(hasZoneNotLineFail1, ticketTypeFail1, zoneFail1)).thenThrow(UserNotFoundException.class);
        when(passengerService.getPrice(hasZoneNotLineFail2, ticketTypeFail2, zoneFail2)).thenThrow(PriceTicketNotFoundException.class);
        when(passengerService.getPrice(hasZoneNotLineFail3, ticketTypeFail3, zoneFail3)).thenThrow(ZoneNotFoundException.class);
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

    @Test
    public void changeAccountTypeTestSuccess() {
        /*HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(imageSucces.toString(), headers);


        ResponseEntity responseEntity = testRestTemplate.exchange("/passengers/change-account-type/"+newPassengerTypeSuccess,
                HttpMethod.POST,httpEntity, Void.class);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        */
    }

    @Test
    public void changeAccountTypeTestFail() {

    }

    @Test
    public void buyTicketTestSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(ticketDtoSuccess, headers);


        ResponseEntity<TicketFrontendDTO> responseEntity = testRestTemplate.exchange("/passengers/buy-ticket",
                HttpMethod.POST,httpEntity, TicketFrontendDTO.class);

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(ticketFrontendDtoSuccess, responseEntity.getBody());
    }

    @Test
    public void buyTicketTestFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(ticketDtoFail, headers);


        ResponseEntity<TicketFrontendDTO> responseEntity = testRestTemplate.exchange("/passengers/buy-ticket",
                HttpMethod.POST,httpEntity, TicketFrontendDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void getPriceTestSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<Double> responseEntity = testRestTemplate.exchange("/passengers/get-price?"
                        + "hasZoneNotLine="+hasZoneNotLineSuccess+"&ticketType="+ticketTypeSuccess.name()+"&zone="+zoneSuccess,
                HttpMethod.GET,httpEntity, Double.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(priceSuccess, responseEntity.getBody().doubleValue(), 0.05);
    }

    @Test
    public void getPriceTestFail1() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<Double> responseEntity = testRestTemplate.exchange("/passengers/get-price?"
                + "hasZoneNotLine="+hasZoneNotLineFail1+"&ticketType="+ticketTypeFail1.name()+"&zone="+zoneFail1,
                HttpMethod.GET,httpEntity, Double.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

    }

    @Test
    public void getPriceTestFail2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<Double> responseEntity = testRestTemplate.exchange("/passengers/get-price?"
                        + "hasZoneNotLine="+hasZoneNotLineFail2+"&ticketType="+ticketTypeFail2.name()+"&zone="+zoneFail2,
                HttpMethod.GET,httpEntity, Double.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void getPriceTestFail3() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<Double> responseEntity = testRestTemplate.exchange("/passengers/get-price?"
                        + "hasZoneNotLine="+hasZoneNotLineFail3+"&ticketType="+ticketTypeFail3.name()+"&zone="+zoneFail3,
                HttpMethod.GET,httpEntity, Double.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void printTicketTestSuccess() {

    }
}
