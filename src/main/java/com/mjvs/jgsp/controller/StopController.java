package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.dto.StopLite2DTO;
import com.mjvs.jgsp.dto.StopLiteDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/stops")
public class StopController
{
    private StopService stopService;

    @Autowired
    public StopController(StopService stopService)
    {
        this.stopService = stopService;
    }

    // Stop name doesn`t need to be unique!
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody StopDTO stopDTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(stopDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Stop));
        }

        Result<Stop> existsResult = stopService.findByLatitudeAndLongitude(
                stopDTO.getLatitude(), stopDTO.getLongitude());
        if(existsResult.hasData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Stop newStop = new Stop(stopDTO.getLatitude(), stopDTO.getLongitude(), stopDTO.getName());
        Result addResult = stopService.save(newStop);
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        return ResponseHelpers.getResponseData(addResult.getData());
    }

    @RequestMapping(value = "/coordinates", method = RequestMethod.POST)
    public ResponseEntity changeCoordinates(@RequestBody StopLiteDTO stopLiteDTO) throws Exception
    {
        Result<Stop> stopResult = stopService.findById(stopLiteDTO.getId());
        if(!stopResult.hasData()) {
            throw new BadRequestException(stopResult.getMessage());
        }

        Result<Stop> existsResult = stopService.findByLatitudeAndLongitude(
                stopLiteDTO.getLatitude(), stopLiteDTO.getLongitude());
        if(existsResult.hasData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Stop stop = stopResult.getData();
        stop.setLatitude(stopLiteDTO.getLatitude());
        stop.setLongitude(stopLiteDTO.getLongitude());

        Result<Boolean> saveResult = stopService.save(stop);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult.getData());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Stop>> getAll()
    {
        Result<List<Stop>> getResult = stopService.getAll();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        return ResponseHelpers.getResponseData(getResult.getData());
    }

    @RequestMapping(value = "/simulation", method = RequestMethod.POST)
    public ResponseEntity receiveCoordinates(@RequestBody String data)
    {
        System.out.println(data);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody StopLite2DTO stopLite2DTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(stopLite2DTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Line));
        }

        Result<Stop> stopResult = stopService.findById(stopLite2DTO.getId());
        if(!stopResult.hasData()) {
            throw new BadRequestException(stopResult.getMessage());
        }

        Stop stop = stopResult.getData();
        stop.setName(stopLite2DTO.getName());

        Result<Boolean> saveResult = stopService.save(stop);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult.getData());
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception
    {
        Result<Stop> stopResult = stopService.findById(id);
        if(!stopResult.hasData()){
            throw new BadRequestException(stopResult.getMessage());
        }

        Stop stop = stopResult.getData();
        Result<Boolean> deleteResult = stopService.delete(stop);
        if(deleteResult.isFailure()) {
            throw new DatabaseException(deleteResult.getMessage());
        }

        return ResponseHelpers.getResponseData(deleteResult);
    }

}
