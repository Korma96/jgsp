package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.helpers.ResponseHelpers;
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

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/line")
public class LineController
{
    @Autowired
    LineService lineService;
/*

    TO DO: Add methods for adding stop and schedule to line

 */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody LineDTO line)
    {
        boolean result = lineService.add(line.getName());
        return ResponseHelpers.getResponseData(result);
    }

    @RequestMapping(value = "/add_stop", method = RequestMethod.POST)
    public ResponseEntity addStop(@RequestBody LineDTO line)
    {
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/add_line", method = RequestMethod.POST)
    public ResponseEntity addSchedule(@RequestBody LineDTO line)
    {
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<List<Line>> getAll()
    {
        List<Line> allLines = lineService.getAll();
        return ResponseHelpers.getResponseData(allLines);
    }

    @RequestMapping(value = "/schedules", method = RequestMethod.POST)
    public ResponseEntity<List<Schedule>> getLineSchedules(@RequestBody LineDTO line)
    {
        List<Schedule> schedules = lineService.getSchedules(line.getName());
        return ResponseHelpers.getResponseData(schedules);
    }

    @RequestMapping(value = "/stops", method = RequestMethod.POST)
    public ResponseEntity<List<Stop>> getLineStops(@RequestBody LineDTO line)
    {
        List<Stop> lineStops = lineService.getLineStops(line.getName());
        return ResponseHelpers.getResponseData(lineStops);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestBody LineDTO line)
    {
        boolean result = lineService.delete(line.getName());
        return ResponseHelpers.getResponseData(result);
    }

    @RequestMapping(value = "/rename")
    public ResponseEntity rename(@RequestBody HashMap<String, String> data)
    {
        boolean result = lineService.rename(data);
        return ResponseHelpers.getResponseData(result);
    }

}
