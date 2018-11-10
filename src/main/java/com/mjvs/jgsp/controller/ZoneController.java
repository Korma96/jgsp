package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.controller.method_helpers.ResponseHelpers;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/zone")
public class ZoneController
{
    @Autowired
    private ZoneService zoneService;

    @Autowired
    private LineService lineService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addZone(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.add(zone.getName());
        return ResponseHelpers.getResponseBasedOnResult(result);
    }

    @RequestMapping(value = "/add_lines", method = RequestMethod.POST)
    public ResponseEntity addZoneLines(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.addLinesToZone(zone.getName(), zone.getLineNames());
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
