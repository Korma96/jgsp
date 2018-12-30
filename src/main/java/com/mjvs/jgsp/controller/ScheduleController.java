package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController
{
    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/dates", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDates() {
        try {
            List<String> dates = scheduleService.getAllDatesFrom();
            return new ResponseEntity<List<String>>(dates, HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
