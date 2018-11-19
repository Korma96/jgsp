package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.helpers.ResponseHelpers;
import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.StopService;
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
@RequestMapping(value = "/stops")
public class StopController
{
    @Autowired
    private StopService stopService;

    @RequestMapping(value = "/coordinates", method = RequestMethod.POST)
    public ResponseEntity receiveCoordinates(@RequestBody String data)
    {
        System.out.println(data);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody StopDTO stop)
    {
        Stop newStop = new Stop(stop.getLatitude(), stop.getLongitude(), stop.getName());
        boolean result = stopService.add(newStop);
        return ResponseHelpers.getResponseData(result);
    }

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public ResponseEntity<List<Stop>> getAllStops()
    {
        List<Stop> allStops = stopService.getAll();
        return ResponseHelpers.getResponseData(allStops);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody HashMap<String, String> data)
    {
        boolean result = stopService.rename(data);
        return ResponseHelpers.getResponseData(result);
    }

    @RequestMapping(value = "/change_coordinates")
    public ResponseEntity changeCoordinates(@RequestBody HashMap<String, Double> data)
    {
        boolean result = stopService.changeCoordinates(data);
        return ResponseHelpers.getResponseData(result);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestBody StopDTO stop)
    {
        boolean result = stopService.delete(stop.getLatitude(), stop.getLongitude());
        return ResponseHelpers.getResponseData(result);
    }
}
