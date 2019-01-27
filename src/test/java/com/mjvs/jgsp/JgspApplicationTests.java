package com.mjvs.jgsp;

import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JgspApplicationTests {

	@Test
	public void contextLoads() {
	}

	public static void prepareLoggedUser(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		final String password = "aaa";
		Passenger passenger = new Passenger("aaa", passwordEncoder.encode(password), UserType.PASSENGER, UserStatus.ACTIVATED, "Aaa", "Aaa", "email_address", "address", PassengerType.OTHER);
		try {
			userService.save(passenger);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken userInfo = new UsernamePasswordAuthenticationToken(passenger.getUsername(), password);
		Authentication authentication = authenticationManager.authenticate(userInfo);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	
	public static void prepareLoggedAdmin(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		final String password = "admin";
		User user = new User("admin",  passwordEncoder.encode(password) , UserType.USER_ADMINISTRATOR, UserStatus.ACTIVATED);
		try {
			userService.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken userInfo = new UsernamePasswordAuthenticationToken(user.getUsername(), password);
		Authentication authentication = authenticationManager.authenticate(userInfo);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
