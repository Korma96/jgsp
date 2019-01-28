package com.mjvs.jgsp.integration_tests.service;

import static com.mjvs.jgsp.JgspApplicationTests.prepareLoggedAdmin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.repository.PassengerRepository;
import com.mjvs.jgsp.repository.UserRepository;
import com.mjvs.jgsp.service.UserService;




@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserAdminServiceTest {

	    @Autowired
	    private PassengerRepository passengerRepository;
	    
	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    AuthenticationManager authenticationManager;
	    
	    private Passenger passenger;

	    
	    private User user;
	    
		@Before
		public void setUp() throws Exception {
			 user = new User("test","test",UserType.USER_ADMINISTRATOR, UserStatus.ACTIVATED);
			 passenger = new Passenger("p", "p",UserType.PASSENGER, UserStatus.ACTIVATED, "p", "p", "p", "p", PassengerType.OTHER, PassengerType.STUDENT);
		}
		
		
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void acceptPassengerRequestAcceptedTest() {
	    	
	        prepareLoggedAdmin(userService, passwordEncoder, authenticationManager);
	        User verifiedBy = new User("faillogged", "faillogged", UserType.USER_ADMINISTRATOR, UserStatus.ACTIVATED);
	        
	        try {
	        	verifiedBy = userService.getLoggedUser();
	        } catch (UserNotFoundException e) {
	            e.printStackTrace();
	        }
	        passenger = passengerRepository.save(passenger);
	       
	        boolean retValue = false ;
	        try {
				retValue = userService.acceptPassengerRequest(passenger.getId(), true);
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
	       
	        assertTrue(retValue);
	        assertEquals(PassengerType.STUDENT, passenger.getPassengerType());
	        assertEquals(verifiedBy, passenger.getVerifiedBy());
	        assertNull(passenger.getNewPassengerType());
	        
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void acceptPassengerRequestDeclinedTest() {
	    	
	        prepareLoggedAdmin(userService, passwordEncoder, authenticationManager);	        
	        passenger = passengerRepository.save(passenger);
	       
	        boolean retValue = false ;
	        try {
				retValue = userService.acceptPassengerRequest(passenger.getId(), false);
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
	       
	        assertTrue(retValue);
	        assertEquals(PassengerType.OTHER, passenger.getPassengerType());
	        assertNull(passenger.getVerifiedBy());
	        assertNull(passenger.getNewPassengerType());
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void saveUsernameExistsTest() {
	    	userRepository.save(user);
	    	boolean retvalue;
	    	retvalue = userService.save("test", "s", UserStatus.ACTIVATED, UserType.USER_ADMINISTRATOR);
	    	assertFalse(retvalue);
	    	
	    	
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void saveDataEmptyTest() {
	    	boolean retvalue;
	    	retvalue = userService.save("", "s", UserStatus.ACTIVATED, UserType.USER_ADMINISTRATOR);
	    	assertFalse(retvalue);
	    	
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void saveSuccessTest() {
	    	boolean retvalue;
	    	retvalue = userService.save(user.getUsername(), user.getPassword(),user.getUserStatus(), user.getUserType());
	    	assertTrue(retvalue);
	    	User expUser =  userRepository.findByUsername("test");
	    	assertEquals(user.getUsername(), expUser.getUsername());
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
		public void deleteUserSuccessTest() {
	    	boolean retValue = false;
	    	User userRet = null;
			try {
				userRet = userRepository.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				retValue = userService.deleteUser(userRet.getId());
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			assertTrue(retValue);
			assertEquals(user.getUsername(), userRepository.findByIdAndDeleted(user.getId(), true).getUsername());
			
		
		}
	    
	    @Transactional
	    @Rollback(true)
	    @Test(expected = UserNotFoundException.class)
		public void deleteUserFailTest() throws UserNotFoundException {
	    	long invalidId = 486513181L;
	    	userService.deleteUser(invalidId);
		}
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void adminActivationDeactivateSuccessTest() {
	    	boolean retValue;
	    	User savedUser = userRepository.save(user);
	    	retValue = userService.adminActivation(savedUser.getId(), true);
	    	assertEquals(UserStatus.DEACTIVATED, userRepository.findByIdAndDeleted(savedUser.getId(), false).getUserStatus());
	    	assertTrue(retValue);
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void adminActivationActivateSuccessTest() {
	    	boolean retValue;
	    	user.setUserStatus(UserStatus.DEACTIVATED);
	    	User savedUser = userRepository.save(user);
	    	retValue = userService.adminActivation(savedUser.getId(), true);
	    	assertEquals(UserStatus.ACTIVATED, userRepository.findByIdAndDeleted(savedUser.getId(), false).getUserStatus());
	    	assertTrue(retValue);
	    }
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void adminActivationFail() {
	    	boolean retValue;
	    	long invalidId = 486513181L;
	    	retValue = userService.adminActivation(invalidId, true);
	    	assertFalse(retValue);
	    	
	    }
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void activatePassengerSuccessTest() {
	    	boolean retValue;
	    	Passenger savedPassengeer = passengerRepository.save(passenger);
	    	passenger.setUserStatus(UserStatus.DEACTIVATED);
	    	retValue = userService.activatePassenger(savedPassengeer.getId(), true);
	    	assertEquals(UserStatus.ACTIVATED, userRepository.findByIdAndDeleted(savedPassengeer.getId(), false).getUserStatus());
	    	assertTrue(retValue);
	    }
	    
	    
	    @Transactional
	    @Rollback(true)
	    @Test
	    public void activatePassengerFailTest() {
	    	boolean retValue;
	    	long invalidId = 486513181L;
	    	retValue = userService.activatePassenger(invalidId, true);
	    	assertFalse(retValue);
	    }

}
