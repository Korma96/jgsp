package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.UserDTO;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.service.ScheduleService;
import com.mjvs.jgsp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;


    @Autowired
    private HttpSession httpSession;


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody UserDTO userDTO) {
        try{
            User user = new User(userDTO.getUsername(), userDTO.getPassword(), UserType.ADMINISTRATOR, UserStatus.ACTIVATED);
            userService.save(user);

            List<MyLocalTime> l = new ArrayList<MyLocalTime>();
            l.add(new MyLocalTime(LocalTime.now()));
            l.add(new MyLocalTime(LocalTime.now()));
            l.add(new MyLocalTime(LocalTime.now()));
            Schedule schedule = new Schedule(DayType.WORKDAY, LocalDate.now(), l);
            scheduleService.save(schedule);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value="/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
        try{
            User u = userService.tryToLogin(userDTO);

            if(u.getUserStatus() != UserStatus.DEACTIVATED){
                httpSession.setAttribute("loggedUser",u);
                return new ResponseEntity(HttpStatus.OK);
            }else{
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}
