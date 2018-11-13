package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.controller.method_helpers.ResponseHelpers;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/zone")
public class ZoneController
{
    @Autowired
    private ZoneService zoneService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.add(zone.getName());
        return ResponseHelpers.getResponseBasedOnBoolean(result);
    }

    @RequestMapping(value = "/add_line", method = RequestMethod.POST)
    public ResponseEntity addLineToZone(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.addLineToZone(zone.getName(), zone.getLineName());
        return ResponseHelpers.getResponseBasedOnBoolean(result);
    }

    @RequestMapping(value = "/remove_line", method = RequestMethod.POST)
    public ResponseEntity removeLineFromZone(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.removeLineFromZone(zone.getName(), zone.getLineName());
        return ResponseHelpers.getResponseBasedOnBoolean(result);
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<List<Zone>> getAllZones()
    {
        List<Zone> allZones = zoneService.getAll();
        return ResponseHelpers.getResponseData(allZones);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestBody ZoneDTO zone)
    {
        boolean result = zoneService.delete(zone.getName());
        return ResponseHelpers.getResponseBasedOnBoolean(result);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody HashMap<String, String> data)
    {
        boolean result = zoneService.rename(data);
        return ResponseHelpers.getResponseBasedOnBoolean(result);
    }
}
