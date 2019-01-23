package com.mjvs.jgsp.simulation.controller;

import com.mjvs.jgsp.simulation.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/simulation")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/add-line-for-show-postions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addFavoriteLine(@RequestBody Long id) {
        try {
            simulationService.addFavoriteLine(id);
        } catch (Exception e) {
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value ="/line-for-show-postions/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity removeFavoriteLine(@PathVariable("id") Long id) {
        try {
            simulationService.removeFavoriteLine(id);
        } catch (Exception e) {
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
