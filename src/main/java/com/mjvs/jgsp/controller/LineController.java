package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.LineDtoIdAndName;
import com.mjvs.jgsp.dto.LineDtoFromFrontend;
import com.mjvs.jgsp.exceptions.LineNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/lines")
public class LineController
{
    @Autowired
    LineService lineService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addLine(@RequestBody LineDtoFromFrontend line)
    {
        boolean result = lineService.add(line.getLineName(), line.getZoneId());

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    /*@RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<List<Line>> getAllLines()
    {
        List<Line> allLines = lineService.getAll();
        return new ResponseEntity<>(allLines, HttpStatus.OK);
    }*/

    @RequestMapping(/*value = "/get_all_names",*/ method = RequestMethod.GET)
    public ResponseEntity<List<LineDtoIdAndName>> getAllLines() {
        List<Line> activeLines = lineService.getActiveLines();

        List<LineDtoIdAndName> activeLinesDto = activeLines.stream()
                .map(LineDtoIdAndName::new)
                .collect(Collectors.toList());

        return new ResponseEntity<List<LineDtoIdAndName>>(activeLinesDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/schedules", method = RequestMethod.POST)
    public ResponseEntity<List<Schedule>> getLineSchedules(@PathVariable("id") Long id)
    {
        List<Schedule> schedules = null;
        try {
            schedules = lineService.getSchedules(id);
        } catch (LineNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }


    /*@RequestMapping(value = "/stops", method = RequestMethod.POST)
    public ResponseEntity<List<Stop>> getLineStops(@RequestBody LineDtoFromFrontend line)
    {
        List<Stop> lineStops = lineService.getLineStops(line.getName());
        return new ResponseEntity<>(lineStops, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/{id}/stops", method = RequestMethod.POST)
    public ResponseEntity<List<Stop>> getLineStops(@PathVariable("id") Long id)
    {
        List<Stop> lineStops = null;
        try {
            lineStops = lineService.getLineStops(id);
        } catch (LineNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineStops, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/delete")
    public ResponseEntity deleteLine(@PathVariable("id") Long id)
    {
        try {
            lineService.delete(id);
        }
        catch (LineNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
