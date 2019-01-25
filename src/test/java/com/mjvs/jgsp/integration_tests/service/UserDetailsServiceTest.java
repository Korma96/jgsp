package com.mjvs.jgsp.integration_tests.service;

import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    @Rollback(true)
    @Test
    public void loadUserByUsernameTestSuccess() {
        User user = new User("aaa", "aaa", UserType.USER_ADMINISTRATOR, UserStatus.ACTIVATED);
        userRepository.save(user);

        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        ArrayList<GrantedAuthority> authorities =  new ArrayList<GrantedAuthority>(userDetails.getAuthorities());
        assertEquals(user.getUserType().name(), authorities.get(0).getAuthority());
    }

    @Transactional
    @Rollback(true)
    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameTestThrowsUsernameNotFoundException() throws UsernameNotFoundException {
        final String wrongUsername = "bbb";
        userDetailsService.loadUserByUsername(wrongUsername);
    }

}