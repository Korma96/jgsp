package com.mjvs.jgsp.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.mjvs.jgsp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.PassengerRepository;
import com.mjvs.jgsp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public User getLoggedUser() throws UserNotFoundException {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth
					.getPrincipal();
			return userRepository.findByUsername(user.getUsername());
		} catch (Exception e) {
			throw new UserNotFoundException();
		}

	}

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public boolean checkTicket(String username, String lineName) throws Exception {

        Passenger passenger = (Passenger) this.getUser(username);
        if(passenger.getUserStatus() == UserStatus.DEACTIVATED){
            return false;
        }


        LocalDateTime dateAndTime = LocalDateTime.now();

        Ticket ticket = null;
        boolean valid = false;
        boolean condition;

        for (int i = 0; i < passenger.getTickets().size(); i++) {
            ticket = passenger.getTickets().get(i);

            System.out.println(ticket.getId());
            System.out.println(ticket.getLineZone().getId());
            System.out.println(ticket.getLineZone().getZone().getId());


            System.out.println(ticket.getStartDateAndTime());
            System.out.println(ticket.getEndDateAndTime());

            LineZone lineZone = ticket.getLineZone();

            if(lineZone instanceof Zone) {
                List<Line> lines = ticket.getLineZone().getZone().getLines();
                System.out.println("zona");
                condition =  lines.stream().filter(line -> line.getName().equals(lineName+"A") || line.getName().equals(lineName+"B"))
                                .count() == 0;
            }
            else {
                Line line = (Line) lineZone;
                condition = !line.getName().equals(lineName+"A") && !line.getName().equals(lineName+"B");
            }

            if(condition){
                continue;
            }


            if (ticket.getStartDateAndTime() != null && ticket.getEndDateAndTime() != null) {

                long dif = computeSubtractTwoDateTime(ticket.getStartDateAndTime(), dateAndTime);

                if (dif >= 0) {
                    dif = computeSubtractTwoDateTime(ticket.getEndDateAndTime(), dateAndTime);
                    if (dif <= 0) {
                        valid = true;
                        break;
                    }
                }
            }


        }

		if (!valid) {
			int num_d = passenger.getNumOfDelicts();
			num_d++;
			passenger.setNumOfDelicts(num_d);

			if (num_d == 3) {
				passenger.setUserStatus(UserStatus.DEACTIVATED);
				this.save(passenger);
			}

			return false;

		}

		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public void save(User user) throws Exception {
		userRepository.save(user);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public User getUser(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public boolean exists(String username) {
		User u = userRepository.findByUsernameAndDeleted(username, false);
		return u != null;
	}

	public long computeSubtractTwoDateTime(LocalDateTime ldt1, LocalDateTime ldt2) {
		long sub = ChronoUnit.SECONDS.between(ldt1, ldt2);
		return sub;
	}

	@Override
	public List<User> getAdmins() {
		return userRepository.findByDeleted(false).stream().filter(user -> user.getUserType() != UserType.PASSENGER)
				.collect(Collectors.toList());
	}

	@Override
	public boolean save(String username, String password, UserStatus userStatus, UserType userType) {
		if (username == null || password == null || userStatus == null || userType == null)
			return false;
		if (username == "" || password == "")
			return false;

		if (exists(username))
			return false;
		if (userType == UserType.PASSENGER)
			return false;

		User user = new User(username, passwordEncoder.encode(password), userType, userStatus);
		try {
			userRepository.save(user);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public boolean deleteUser(Long id) throws UserNotFoundException {
		User user = userRepository.findByIdAndDeleted(id, false);
		if (user == null)
			throw new UserNotFoundException();

		user.setDeleted(true);
		userRepository.save(user);
		return true;
	}

	@Override
	public boolean acceptPassengerRequest(Long id, boolean accepted) throws UserNotFoundException {
		Passenger passenger = passengerRepository.findById(id);
		User userAdmin = getLoggedUser();

		if (passenger != null) {
			if(accepted) {
				passenger.setPassengerType(passenger.getNewPassengerType());
				passenger.setNewPassengerType(null);
				passenger.setVerifiedBy(userAdmin);
				userRepository.save(passenger);
			}
			else {
				passenger.setNewPassengerType(null);
				userRepository.save(passenger);

			}
			return true;
		}

		return false;
	}

}