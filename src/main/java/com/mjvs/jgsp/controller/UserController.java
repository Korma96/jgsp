package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.AdministratorDTO;
import com.mjvs.jgsp.model.Administrator;
import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Schedule;
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

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    //@Autowired
    //private ScheduleService scheduleService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody AdministratorDTO administratorDTO) {
        try{
            Administrator  administrator = new Administrator(administratorDTO.getUsername(), administratorDTO.getPassword());
            userService.save(administrator);

            //Schedule schedule = new Schedule(DayType.WORKDAY, LocalDate.now(), new LocalTime[] {LocalTime.now(), LocalTime.now(), LocalTime.now()});
            //scheduleService.save(schedule);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
