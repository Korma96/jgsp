package com.mjvs.jgsp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.dto.UserBackendDTO;
import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.security.TokenUtils;
import com.mjvs.jgsp.service.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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
	ZoneService zoneService;
	
	@MockBean
	LineService lineService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
	TokenUtils tokenUtils;
	
	private HttpHeaders headers;
	
	private UserBackendDTO userDtoSuccessAccept;
	private UserBackendDTO userDtoUnsuccessAccept;
	private UserBackendDTO userDtoSuccessDecline;
	private UserBackendDTO userDtoUnsuccessDecline;
	private UserBackendDTO userDtoFail;
	
	private String accessToken;
	private UserDetails adminDetails;
	private UserDTO adminDTO;
	
	@Before
	public void setUp() throws Exception {
		adminDTO = new UserDTO("user1","111");

		userDtoSuccessAccept = new UserBackendDTO(1L,"1", "1", UserType.TRANSPORT_ADMINISTRATOR);
		userDtoUnsuccessAccept = new UserBackendDTO(2L,"2", "2", UserType.TRANSPORT_ADMINISTRATOR);
		userDtoSuccessDecline = new UserBackendDTO(3L,"3", "3", UserType.TRANSPORT_ADMINISTRATOR);
		userDtoUnsuccessDecline = new UserBackendDTO(4L,"4", "4", UserType.TRANSPORT_ADMINISTRATOR);
		userDtoFail = new UserBackendDTO(5L,"5", "5", UserType.TRANSPORT_ADMINISTRATOR);
		
		List<GrantedAuthority> garantedAuthorities = AuthorityUtils.createAuthorityList(UserType.USER_ADMINISTRATOR.toString());

		adminDetails = new org.springframework.security.core.userdetails.User(adminDTO.getUsername(), adminDTO.getPassword(), garantedAuthorities);
        when(userDetailsService.loadUserByUsername(adminDTO.getUsername())).thenReturn(adminDetails);
        accessToken = "token1";
        
        when(tokenUtils.generateToken(adminDetails)).thenReturn(accessToken);


        when(tokenUtils.getUsernameFromToken(accessToken)).thenReturn(adminDTO.getUsername());
        when(tokenUtils.validateToken(accessToken, adminDetails)).thenReturn(true);

        headers = new HttpHeaders();
	    headers.add("X-Auth-Token", accessToken);
	    
	    Line line = new Line(1L,"1A");
	    Line line2 = new Line(2L,"1B");
	    Zone zone = new Zone(3L, "prva");
	    
	    LocalDateTime dateTest = LocalDateTime.of(2019, Month.JANUARY, 21, 19, 30, 40);

	    
		ArrayList<Ticket> tickets = new ArrayList<>();
		tickets.add(new Ticket(1L,dateTest , dateTest, TicketType.DAILY, PassengerType.OTHER ,65, line2));
		tickets.add(new Ticket(2L, dateTest, dateTest, TicketType.MONTHLY, PassengerType.OTHER ,935, line));
		tickets.add(new Ticket(3L, dateTest, dateTest, TicketType.YEARLY, PassengerType.OTHER , 1800 , line));
		tickets.add(new Ticket(4L, dateTest, dateTest, TicketType.DAILY, PassengerType.OTHER , 65 , zone));
		tickets.add(new Ticket(5L, dateTest, dateTest, TicketType.DAILY, PassengerType.STUDENT ,135, zone));

		when(userService.save(userDtoSuccessAccept.getUsername(), userDtoSuccessAccept.getPassword(), UserStatus.ACTIVATED, userDtoSuccessAccept.getUserType())).thenReturn(true);
		when(userService.save(userDtoFail.getUsername(), userDtoFail.getPassword(), UserStatus.ACTIVATED, userDtoFail.getUserType())).thenReturn(false);
		when(ticketService.getAll()).thenReturn(tickets);
		when(userService.acceptPassengerRequest(userDtoSuccessAccept.getId(), true)).thenReturn(true);
		when(userService.acceptPassengerRequest(userDtoUnsuccessAccept.getId(), true)).thenReturn(false);
		when(userService.acceptPassengerRequest(userDtoSuccessDecline.getId(), false)).thenReturn(true);
		when(userService.acceptPassengerRequest(userDtoUnsuccessDecline.getId(), false)).thenReturn(false);
		
		when(zoneService.findByName("prva")).thenReturn(zone);
		when(lineService.findByName("1A")).thenReturn(line);
		when(lineService.findByName("1B")).thenReturn(line2);


	}
	
	@Test
	public void testAddSuccess() {
	    HttpEntity<UserBackendDTO> requestEntity = new HttpEntity<UserBackendDTO>(userDtoSuccessAccept, headers);
		ResponseEntity<Boolean> response = testRestTemplate.exchange("/userAdmin/add-admin", HttpMethod.POST, requestEntity, Boolean.class);
		boolean added = response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(added);
	}
	
	@Test
	public void testAddFail() {
	    HttpEntity<UserBackendDTO> requestEntity = new HttpEntity<UserBackendDTO>(userDtoFail, headers);
		ResponseEntity<Boolean> response = testRestTemplate.exchange("/userAdmin/add-admin", HttpMethod.POST, requestEntity, Boolean.class);
		boolean added = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(added);
	}

	@Test
	public void acceptPassengerRequestTest() {
	    HttpEntity<Boolean> requestEntity = new HttpEntity<Boolean>(true, headers);
	    ResponseEntity<Boolean> response = testRestTemplate.exchange("/userAdmin/request-review/"+userDtoSuccessAccept.getId(), HttpMethod.PUT, requestEntity, Boolean.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody());

	}


	@Test
	public void testDelete() {
		

		
	}

	@Test
	public void testAcceptPassengerRequest() {

	}
	
	@Test
	public void testGeneralReport() throws ParseException {
		ReportDTO r = new ReportDTO(0,3,1,1,3000);
		String startDateStr = "2018-8-01";
		String endDateStr = "2019-12-1";
		
		HttpEntity requestEntity = new HttpEntity(headers);
		ResponseEntity<ReportDTO> response = testRestTemplate.exchange("/userAdmin/general-report?startDate="+ startDateStr +
														"&endDate="+endDateStr, HttpMethod.GET, requestEntity, ReportDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		ReportDTO retValue = response.getBody();
		System.out.println(retValue.getDaily() + " mesec " + retValue.getMonthly() + " jedna " +retValue.getOneTime() + " prof " +retValue.getProfit() + " godina: " + retValue.getYearly());
		assertEquals(r.getDaily(),retValue.getDaily());
		assertEquals(r.getOneTime(),retValue.getOneTime());
		assertEquals(r.getMonthly(),retValue.getMonthly());
		double myPi = 22.0d / 7.0d;
		assertEquals(myPi,r.getProfit(),retValue.getProfit());
		assertEquals(r.getYearly(), retValue.getYearly());

	}
	
	@Test
	@Ignore
	public void testDailyGeneralReport() throws ParseException {
		ReportDTO r = new ReportDTO(0,3,1,1,3000);
		String startDateStr = "2019-01-21";
		
		HttpEntity requestEntity = new HttpEntity(headers);
		ResponseEntity<ReportDTO> response = testRestTemplate.exchange("/userAdmin/daily-general-report?startDate="+ startDateStr,
										HttpMethod.GET, requestEntity, ReportDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		ReportDTO retValue = response.getBody();
		System.out.println(retValue.getDaily() + " mesec " + retValue.getMonthly() + " jedna " +retValue.getOneTime() + " prof " +retValue.getProfit() + " godina: " + retValue.getYearly());
		assertEquals(r.getDaily(),retValue.getDaily());
		assertEquals(r.getOneTime(),retValue.getOneTime());
		assertEquals(r.getMonthly(),retValue.getMonthly());
		double myPi = 22.0d / 7.0d;
		assertEquals(myPi,r.getProfit(),retValue.getProfit());
		assertEquals(r.getYearly(), retValue.getYearly());

	}
	
	
	@Test
	public void testLineZoneGeneralReport() throws ParseException {
		ReportDTO r = new ReportDTO(0,2,0,0,180);
		String startDateStr = "2018-08-01";
		String endDateStr = "2019-12-01";
		String name = "prva";
		HttpEntity requestEntity = new HttpEntity(headers);
		ResponseEntity<ReportDTO> response = testRestTemplate.exchange("/userAdmin/line-zone-report?line_zone_name="+
														name+"&startDate="+startDateStr+"&endDate="+endDateStr, HttpMethod.GET, requestEntity, ReportDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ReportDTO retValue = response.getBody();
		System.out.println(retValue.getDaily() + " mesec " + retValue.getMonthly() + " jedna " +retValue.getOneTime() + " prof " +retValue.getProfit() + " godina: " + retValue.getYearly());
		assertEquals(r.getDaily(),retValue.getDaily());
		assertEquals(r.getOneTime(),retValue.getOneTime());
		assertEquals(r.getMonthly(),retValue.getMonthly());
		double myPi = 22.0d / 7.0d;
		assertEquals(myPi,r.getProfit(), retValue.getProfit());
		assertEquals(r.getYearly(), retValue.getYearly());

	}
	
	
	@Test
	@Ignore
	public void testDailyLineZoneReport() throws ParseException {
		ReportDTO r = new ReportDTO(0,1,1,1,2800);
		String reqDateStr = "2019-01-21";
		String name = "1";
		HttpEntity requestEntity = new HttpEntity(headers);
		ResponseEntity<ReportDTO> response = testRestTemplate.exchange("/userAdmin/line_zone_daily_report?line_zone_name="+
														name+"&requested_date="+reqDateStr, HttpMethod.GET, requestEntity, ReportDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ReportDTO retValue = response.getBody();
		System.out.println(retValue.getDaily() + " mesec " + retValue.getMonthly() + " jedna " +retValue.getOneTime() + " prof " +retValue.getProfit() + " godina: " + retValue.getYearly());
		assertEquals(r.getDaily(),retValue.getDaily());
		assertEquals(r.getOneTime(),retValue.getOneTime());
		assertEquals(r.getMonthly(),retValue.getMonthly());
		double myPi = 22.0d / 7.0d;
		assertEquals(myPi,r.getProfit(),retValue.getProfit());
		assertEquals(r.getYearly(), retValue.getYearly());

	}
	
	
	@Test
	@Ignore
	public void testGeneralReportBadDate() throws ParseException {
		String startDateStr = "2018-08-01";
		String endDateStr = "2019-13-01";
		
		HttpEntity requestEntity = new HttpEntity(headers);
		ResponseEntity<ReportDTO> response = testRestTemplate.exchange("/userAdmin/general-report?startDate="+ startDateStr +
														"&endDate="+endDateStr, HttpMethod.GET, requestEntity, ReportDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testGeneralReportBadDates() throws ParseException {
		String startDateStr = "2018-12-01";
		String endDateStr = "2018-11-01";
		
		HttpEntity requestEntity = new HttpEntity(headers);
		ResponseEntity<ReportDTO> response = testRestTemplate.exchange("/userAdmin/general-report?startDate="+ startDateStr +
														"&endDate="+endDateStr, HttpMethod.GET, requestEntity, ReportDTO.class);
		
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	

}
