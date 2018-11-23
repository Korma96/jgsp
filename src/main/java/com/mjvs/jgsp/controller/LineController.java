package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.dto.LineLiteDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lines")
public class LineController
{
    private LineService lineService;

    @Autowired
    public LineController(LineService lineService)
    {
        this.lineService = lineService;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody LineLiteDTO lineDTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(lineDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Line));
        }

        Result<Boolean> existsResult = lineService.exists(lineDTO.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Result addResult = lineService.save(new Line(lineDTO.getName()));
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        return ResponseHelpers.getResponseData(addResult.getData());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<LineDTO>> getAll()
    {
        Result<List<LineDTO>> getResult = lineService.getAll();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        return ResponseHelpers.getResponseData(getResult.getData());
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<List<LineDTO>> getActiveLines()
    {
        Result<List<LineDTO>> getResult = lineService.getActiveLines();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        return ResponseHelpers.getResponseData(getResult.getData());
    }

    @RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET)
    public ResponseEntity<List<Schedule>> getLineSchedules(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<Schedule> schedules = line.getSchedules();
        List<Schedule> latestSchedules = lineService.getLatestSchedules(schedules);

        return ResponseHelpers.getResponseData(latestSchedules);
    }

    @RequestMapping(value = "/{id}/stops", method = RequestMethod.POST)
    public ResponseEntity<List<Stop>> getLineStops(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<Stop> sortedStops = lineService.getSortedStopsById(line.getStops());

        return ResponseHelpers.getResponseData(sortedStops);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        Result<Boolean> deleteResult = lineService.delete(line);
        if(deleteResult.isFailure()) {
            throw new DatabaseException(deleteResult.getMessage());
        }

        return ResponseHelpers.getResponseData(deleteResult);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody LineDTO lineDTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(lineDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Line));
        }

        Result<Boolean> existsResult = lineService.exists(lineDTO.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Result<Line> lineResult = lineService.findById(lineDTO.getId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        line.setName(lineDTO.getName());

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult.getData());
    }
}
