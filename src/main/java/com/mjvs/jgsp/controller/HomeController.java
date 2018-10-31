package com.mjvs.jgsp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/home_page")
public class HomeController
{
    @RequestMapping(value = "/coordinates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity receiveCoordinates(@RequestBody String data)
    {
        System.out.println(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
