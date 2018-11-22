package com.mjvs.jgsp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.controller.method_helpers.ResponseHelpers;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;

@RestController
@RequestMapping(value = "/zones")
public class ZoneController
{
    @Autowired
    private ZoneService zoneService;

    @Autowired
    private LineService lineService;

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addZone(@RequestBody String zoneName)
    {
        boolean result = zoneService.add(zoneName);
        return ResponseHelpers.getResponseBasedOnResult(result);
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @RequestMapping(value = "/add-with-lines", method = RequestMethod.POST)
    public ResponseEntity addZoneWithLines(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.addZoneWithLines(zone.getName(), zone.getLineNames());
        return ResponseHelpers.getResponseBasedOnResult(result);
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<List<Zone>> getAllZones()
    {
        List<Zone> allZones = zoneService.getAll();
        return ResponseHelpers.getResponseData(allZones);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteZone(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.delete(zone.getName());
        return ResponseHelpers.getResponseBasedOnResult(result);
    }
}
