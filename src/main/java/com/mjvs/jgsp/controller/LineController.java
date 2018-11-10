package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/line")
public class LineController
{
    @Autowired
    LineService lineService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addLine(@RequestBody LineDTO line)
    {
        boolean result = lineService.add(line.getName());

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<List<Line>> getAllLines()
    {
        List<Line> allLines = lineService.getAll();
        return new ResponseEntity<>(allLines, HttpStatus.OK);
    }

    @RequestMapping(value = "/schedules", method = RequestMethod.POST)
    public ResponseEntity<List<Schedule>> getLineSchedules(@RequestBody LineDTO line)
    {
        List<Schedule> schedules = lineService.getSchedules(line.getName());
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }


    @RequestMapping(value = "/stops", method = RequestMethod.POST)
    public ResponseEntity<List<Stop>> getLineStops(@RequestBody LineDTO line)
    {
        List<Stop> lineStops = lineService.getLineStops(line.getName());
        return new ResponseEntity<>(lineStops, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete")
    public ResponseEntity deleteLine(@RequestBody LineDTO line)
    {
        boolean result = lineService.delete(line.getName());

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
