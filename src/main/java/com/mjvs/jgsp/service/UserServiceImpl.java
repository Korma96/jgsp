package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public User getLoggedUser() throws UserNotFoundException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            return userRepository.findByUsername(user.getUsername());
        } catch (Exception e) {
            throw new UserNotFoundException();
        }

    }

    public boolean checkTicket(String username, Long id) throws Exception {

        Passenger passenger = (Passenger) this.getUser(username);

        LocalDateTime dateAndTime = LocalDateTime.now();

        Ticket ticket = null;
        boolean valid = false;
        for (int i = 0; i < passenger.getTickets().size(); i++) {
            ticket = passenger.getTickets().get(i);

            if(!ticket.getLineZone().getId().equals(id)){
                if(ticket.getLineZone() instanceof Zone) {
                    List<Line> lines = ticket.getLineZone().getZone().getLines();

                    boolean condition =  lines.stream().filter(line -> line.getId() == id).count() == 0;

                    if(condition){
                        continue;
                    }
                }
                else {
                    continue;
                }

            }


            if (ticket.getStartDateAndTime() != null && ticket.getEndDateAndTime() != null) {

                long dif = computeSubtractTwoDateTime(dateAndTime, ticket.getStartDateAndTime());

                if (dif >= 0) {
                    dif = computeSubtractTwoDateTime(dateAndTime, ticket.getEndDateAndTime());
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
    public User getUser(String username, String password){
    	return userRepository.findByUsernameAndPassword(username, password);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public boolean exists(String username)
    {
        User u = userRepository.findByUsername(username);
        return u != null;
    }

    public long computeSubtractTwoDateTime(LocalDateTime ldt1, LocalDateTime ldt2) {
        long sub = ChronoUnit.SECONDS.between(ldt1, ldt2);
        return sub;
    }
    
}