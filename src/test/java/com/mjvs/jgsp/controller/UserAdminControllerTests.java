package com.mjvs.jgsp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.dto.UserBackendDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.security.TokenUtils;
import com.mjvs.jgsp.service.TicketService;
import com.mjvs.jgsp.service.UserDetailsServiceImpl;
import com.mjvs.jgsp.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class UserAdminControllerTests {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	
	@MockBean
	private TicketService ticketService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    TokenUtils tokenUtils;
	
	
	private UserBackendDTO userDtoSuccess;
	private UserBackendDTO userDtoFail;
	
	 private String accessToken1;
	 private  UserDetails userDetails1;
	
	 private UserDTO userDTO1;
	
	@Before
	public void setUp() throws Exception {
		userDTO1 = new UserDTO("user1","111");
		
		userDtoSuccess = new UserBackendDTO(1L,"1", "1", UserType.TRANSPORT_ADMINISTRATOR);
		userDtoFail = new UserBackendDTO("", "1", UserType.TRANSPORT_ADMINISTRATOR);
		
		List<GrantedAuthority> garantedAuthorities = AuthorityUtils.createAuthorityList(UserType.USER_ADMINISTRATOR.toString());

        userDetails1 = new org.springframework.security.core.userdetails.User(userDTO1.getUsername(), userDTO1.getPassword(), garantedAuthorities);
        when(userDetailsService.loadUserByUsername(userDTO1.getUsername())).thenReturn(userDetails1);
        accessToken1 = "token1";
        
        when(tokenUtils.generateToken(userDetails1)).thenReturn(accessToken1);


        when(tokenUtils.getUsernameFromToken(accessToken1)).thenReturn(userDTO1.getUsername());
        when(tokenUtils.validateToken(accessToken1, userDetails1)).thenReturn(true);


		ArrayList<Ticket> tickets = new ArrayList<>();
		tickets.add(new Ticket(1L, LocalDateTime.now(), LocalDateTime.now(), TicketType.DAILY, PassengerType.OTHER ,65, new Zone("1", null)));
		tickets.add(new Ticket(2L, LocalDateTime.now(), LocalDateTime.now(), TicketType.MONTHLY, PassengerType.OTHER ,935, new Line("2")));
		tickets.add(new Ticket(3L, LocalDateTime.now(), LocalDateTime.now(), TicketType.YEARLY, PassengerType.OTHER , 1800 , new Zone("3", null)));
		tickets.add(new Ticket(4L, LocalDateTime.now(), LocalDateTime.now(), TicketType.DAILY, PassengerType.OTHER , 65 , new Zone("1", null)));
		tickets.add(new Ticket(5L, LocalDateTime.now(), LocalDateTime.now(), TicketType.DAILY, PassengerType.STUDENT ,135, new Zone("1", null)));
		when(userService.save(userDtoSuccess.getUsername(), userDtoSuccess.getPassword(), UserStatus.ACTIVATED, userDtoSuccess.getUserType())).thenReturn(true);
		when(userService.save(userDtoFail.getUsername(), userDtoFail.getPassword(), UserStatus.ACTIVATED, userDtoFail.getUserType())).thenReturn(false);
		when(ticketService.getAll()).thenReturn(tickets);
		when(ticketService.getAll()).thenReturn(tickets);
		when(userService.acceptPassengerRequest(userDtoFail.getId(), true)).thenThrow(UserNotFoundException.class);
		when(userService.acceptPassengerRequest(userDtoSuccess.getId(), true)).thenThrow(UserNotFoundException.class);

		
		

	}
	
	@Test
	public void testAddSuccess() {
		/*ResponseEntity<Boolean> response = testRestTemplate.postForEntity("/userAdmin/add-admin", userDtoSuccess, Boolean.class);
		boolean added = response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(added);*/
	}
	
	@Test
	public void testAddFail() {
		/*ResponseEntity<Boolean> response = testRestTemplate.postForEntity("/userAdmin/add-admin", userDtoFail, Boolean.class);
		boolean added = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(added);*/
	}

	@Test
	
	public void acceptPassengerRequestException() {
		/*
	    HttpEntity<Boolean> requestEntity = new HttpEntity<Boolean>(true);
		
	    ResponseEntity response = testRestTemplate.exchange("/request-review/"+userDtoSuccess.getId(), HttpMethod.PUT, requestEntity, Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());*/

	}


	@Test
	public void testDelete() {
		
		fail("Not yet implemented");
		
	}

	@Test
	public void testAcceptPassengerRequest() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeclinePassengerRequest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGeneralReport() throws ParseException {
		/*ReportDTO r = new ReportDTO(0,3,1,1,3000);
		String startDateStr = "2018-08-01";
		String endDateStr = "2019-12-01";
		ResponseEntity<ReportDTO> response = testRestTemplate.getForEntity("/userAdmin//line-zone-report?id=startDate="+
														startDateStr+"&endDate="+endDateStr, ReportDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ReportDTO retValue = response.getBody();
		System.out.println(retValue.getDaily() + " mesec " + retValue.getMonthly() + " jedna " +retValue.getOneTime() + " prof " +retValue.getProfit() + " godina: " + retValue.getYearly());
		assertEquals(r.getDaily(),retValue.getDaily());
		assertEquals(r.getOneTime(),retValue.getOneTime());
		assertEquals(r.getMonthly(),retValue.getMonthly());
		double myPi = 22.0d / 7.0d;
		assertEquals(myPi,r.getProfit(),retValue.getProfit());
		assertEquals(r.getYearly(), retValue.getYearly());*/

	}
	
	@Test
	public void testGeneralLineZoneReport() throws ParseException {
		/*ReportDTO r = new ReportDTO(0,3,1,1,3000);
		String startDateStr = "2018-08-01";
		String endDateStr = "2019-12-01";
		
		ResponseEntity<ReportDTO> response = testRestTemplate.getForEntity("/userAdmin/general-report?startDate="+
														startDateStr+"&endDate="+endDateStr, ReportDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ReportDTO retValue = response.getBody();
		System.out.println(retValue.getDaily() + " mesec " + retValue.getMonthly() + " jedna " +retValue.getOneTime() + " prof " +retValue.getProfit() + " godina: " + retValue.getYearly());
		assertEquals(r.getDaily(),retValue.getDaily());
		assertEquals(r.getOneTime(),retValue.getOneTime());
		assertEquals(r.getMonthly(),retValue.getMonthly());
		double myPi = 22.0d / 7.0d;
		assertEquals(myPi,r.getProfit(),retValue.getProfit());
		assertEquals(r.getYearly(), retValue.getYearly());*/

	}
	
	
	
	@Test
	public void testGeneralReportBadDate() throws ParseException {
		/*String startDateStr = "2018-08-01";
		String endDateStr = "2019-13-01";
		
		ResponseEntity<ReportDTO> response = testRestTemplate.getForEntity("/userAdmin/general-report?startDate="+
														startDateStr+"&endDate="+endDateStr, ReportDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());*/
	}
	
	@Test
	public void testGeneralReportBadDates() throws ParseException {
		/*String startDateStr = "2018-12-01";
		String endDateStr = "2018-11-01";
		
		ResponseEntity<ReportDTO> response = testRestTemplate.getForEntity("/userAdmin/general-report?startDate="+
														startDateStr+"&endDate="+endDateStr, ReportDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());*/
	}
	

}
