package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.*;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ScheduleService;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ResponseHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/line")
public class LineController extends ExtendedBaseController<Line>
{
    private LineService lineService;
    private StopService stopService;
    private ScheduleService scheduleService;

    @Autowired
    public LineController(LineService lineService, StopService stopService, ScheduleService scheduleService)
    {
        super(lineService);
        this.lineService = lineService;
        this.stopService = stopService;
        this.scheduleService = scheduleService;
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
    public ResponseEntity<List<BaseDTO>> getAll()
    {
        Result<List<Line>> getResult = lineService.getAll();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        getResult.getData().sort(new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                int num1 = extractInt(o1.getName());
                int num2 = extractInt(o2.getName());

                if(num1 == 0 && num2 == 0) return o1.getName().compareTo(o2.getName());

                return num1 - num2;
            }

            int extractInt(String s) {
                String sCopy = new String(s);
                String num = sCopy.replaceAll("\\D", "");
                // return 0 if no digits found
                if(num.isEmpty()) return 0;
                else {
                    if(s.startsWith(num)) return Integer.parseInt(num);

                    return 0;
                }

            }
        });
        List<BaseDTO> lineDTOs = LineConverter.ConvertLinesToBaseDTOs(getResult.getData());
        return ResponseHelpers.getResponseData(lineDTOs);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<List<BaseDTO>> getActiveLines()
    {
        Result<List<BaseDTO>> getResult = lineService.getActiveLines();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        return ResponseHelpers.getResponseData(getResult.getData());
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
    public ResponseEntity<List<StopDTO>> getLineStops(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<StopDTO> sortedStopDTOs = lineService.getSortedStopsById(line.getStops());

        return ResponseHelpers.getResponseData(sortedStopDTOs);
    }

}