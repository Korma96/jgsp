package com.mjvs.jgsp.controller;


import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.security.TokenUtils;
import com.mjvs.jgsp.service.UserDetailsServiceImpl;
import com.mjvs.jgsp.service.UserService;
import org.hibernate.mapping.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    TokenUtils tokenUtils;



    private String accessToken1;
    private String accessToken2;
    private String accessToken3;

    private  UserDetails userDetails1;
    private  UserDetails userDetails2;
    private  UserDetails userDetails3;

    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private UserDTO userDTO3;

    @Before
    public void setUp() throws Exception {
        userDTO1 = new UserDTO("user1","111");
        userDTO2 = new UserDTO("user2","222");
        userDTO3 = new UserDTO("user3","333");
        List<GrantedAuthority> garantedAuthorities = AuthorityUtils.createAuthorityList(UserType.CONTROLLOR.toString());

        userDetails1 = new org.springframework.security.core.userdetails.User(userDTO1.getUsername(), userDTO1.getPassword(), garantedAuthorities);
        when(userDetailsService.loadUserByUsername(userDTO1.getUsername())).thenReturn(userDetails1);
        accessToken1 = "token1";

        userDetails2 = new org.springframework.security.core.userdetails.User(userDTO2.getUsername(), userDTO2.getPassword(), garantedAuthorities);
        when(userDetailsService.loadUserByUsername(userDTO2.getUsername())).thenReturn(userDetails2);
        accessToken2 = "token2";

        userDetails3 = new org.springframework.security.core.userdetails.User(userDTO3.getUsername(), userDTO3.getPassword(), garantedAuthorities);
        when(userDetailsService.loadUserByUsername(userDTO3.getUsername())).thenReturn(userDetails3);
        accessToken3 = "token3";


        when(tokenUtils.generateToken(userDetails1)).thenReturn(accessToken1);


        when(tokenUtils.getUsernameFromToken(accessToken1)).thenReturn(userDTO1.getUsername());
        when(tokenUtils.validateToken(accessToken1, userDetails1)).thenReturn(true);

        when(tokenUtils.getUsernameFromToken(accessToken2)).thenReturn(userDTO2.getUsername());
        when(tokenUtils.validateToken(accessToken2, userDetails2)).thenReturn(true);

        when(tokenUtils.getUsernameFromToken(accessToken3)).thenReturn(userDTO3.getUsername());
        when(tokenUtils.validateToken(accessToken3, userDetails3)).thenReturn(true);


        when(userService.checkTicket(userDetails1.getUsername())).thenReturn(true);
        when(userService.checkTicket(userDetails2.getUsername())).thenReturn(false);
        when(userService.checkTicket(userDetails3.getUsername())).thenThrow(Exception.class);
    }


    @Test
    public void loginTest(){

        HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDTO1);

        ResponseEntity<HashMap> responseEntity = testRestTemplate.exchange("/users/login",HttpMethod.PUT,httpEntity,HashMap.class);

        HashMap<String, String> hmToken = responseEntity.getBody();
        String token = hmToken.get("token");

        assertNotNull(token);
        assertEquals(accessToken1,token);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(accessToken1.length(),token.length());


    }

    @Test
    public void checkTicketTestSuccess(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken1);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/users/checkticket/"+userDTO1.getUsername(),HttpMethod.PUT,httpEntity, Boolean.class);

        boolean valid = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertTrue(valid);
    }


    @Test
    public void checkTicketTestUnsuccess(){
        HttpHeaders h = new HttpHeaders();
        h.add("X-Auth-Token",accessToken2);
        HttpEntity httpEntity = new HttpEntity(h);

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/users/checkticket/"+userDTO2.getUsername(),HttpMethod.PUT,httpEntity, Boolean.class);

        boolean valid = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertFalse(valid);
    }

    @Test
    public void checkTicketTestException(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token",accessToken3);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/users/checkticket/"+userDTO3.getUsername(),HttpMethod.PUT,httpEntity, Boolean.class);

        Boolean valid = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertNull(valid);
    }

}
