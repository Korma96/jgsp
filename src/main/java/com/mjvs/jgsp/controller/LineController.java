package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.dto.LineLiteDTO;
import com.mjvs.jgsp.dto.LineWithScheduleDTO;
import com.mjvs.jgsp.dto.LineWithStopDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ScheduleService;
import com.mjvs.jgsp.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/line")
public class LineController
{
    private LineService lineService;
    private StopService stopService;
    private ScheduleService scheduleService;

    @Autowired
    public LineController(LineService lineService, StopService stopService, ScheduleService scheduleService)
    {
        this.lineService = lineService;
        this.stopService = stopService;
        this.scheduleService = scheduleService;
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

        return ResponseHelpers.getResponseData(addResult);
    }

    @RequestMapping(value = "/stop/add", method = RequestMethod.POST)
    public ResponseEntity addStopToLine(@RequestBody LineWithStopDTO lineWithStopDTO) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineWithStopDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Result<Stop> stopResult = stopService.findById(lineWithStopDTO.getLineId());
        if(!stopResult.hasData()) {
            throw new BadRequestException(stopResult.getMessage());
        }

        Line line = lineResult.getData();
        Stop stop = stopResult.getData();
        if(line.getStops().contains(stop)) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Line, line.getId(), StringConstants.Stop, stop.getId()));
        }

        line.getStops().add(stop);
        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @RequestMapping(value = "schedule/add", method = RequestMethod.POST)
    public ResponseEntity addScheduleToLine(LineWithScheduleDTO lineWithScheduleDTO) throws Exception
    {

        Result<Line> lineResult = lineService.findById(lineWithScheduleDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Result<Schedule> scheduleResult = scheduleService.findById(lineWithScheduleDTO.getScheduleId());
        if(!scheduleResult.hasData()) {
            throw new BadRequestException(scheduleResult.getMessage());
        }

        Line line = lineResult.getData();
        Schedule schedule = scheduleResult.getData();
        if(line.getSchedules().contains(schedule)) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Line, line.getId(), StringConstants.Schedule, schedule.getId()));
        }

        line.getSchedules().add(schedule);
        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<LineDTO>> getAll()
    {
        Result<List<Line>> getResult = lineService.getAll();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        List<LineDTO> lineDTOs = LineConverter.ConvertLinesToLineDTOs(getResult.getData());
        return ResponseHelpers.getResponseData(lineDTOs);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity getActiveLines()
    {
        Result<List<LineDTO>> getResult = lineService.getActiveLines();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        return ResponseHelpers.getResponseData(getResult);
    }

    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.GET)
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

    @RequestMapping(value = "/{id}/stop", method = RequestMethod.GET)
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

        return ResponseHelpers.getResponseData(saveResult);
    }
}
