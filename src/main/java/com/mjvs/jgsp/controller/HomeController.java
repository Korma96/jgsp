package com.mjvs.jgsp.controller;


import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.model.Line;
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
@RequestMapping(value = "/home_page")
public class HomeController
{
    @Autowired
    LineService lineService;

    @RequestMapping(value = "/coordinates", method = RequestMethod.POST)
    public ResponseEntity receiveCoordinates(@RequestBody String data)
    {
        System.out.println(data);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/lines", method = RequestMethod.GET)
    public ResponseEntity<List<Line>> getAllLines()
    {
        List<Line> allLines = lineService.getAll();
        return new ResponseEntity<>(allLines, HttpStatus.OK);
    }

    @RequestMapping(value = "/line_stops", method = RequestMethod.POST)
    public ResponseEntity<List<Stop>> getLineStops(@RequestBody LineDTO line)
    {
        List<Stop> lineStops = lineService.getLineStops(line.getName());
        return new ResponseEntity<>(lineStops, HttpStatus.OK);
    }




}
