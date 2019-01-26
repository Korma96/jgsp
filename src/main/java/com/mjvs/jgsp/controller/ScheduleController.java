package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.ScheduleDTO;
import com.mjvs.jgsp.helpers.ResponseHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.MyLocalTime;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController
{
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private LineService lineService;

    @RequestMapping(value = "/dates", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDates() {
        try {
            List<String> dates = scheduleService.getAllDatesFrom();
            return new ResponseEntity<List<String>>(dates, HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/dates", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getLineDates(@PathVariable("id") long lineId) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineId);
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<String> lineDatesFrom = scheduleService.getLineDatesFrom(line);

        return ResponseHelpers.getResponseData(lineDatesFrom);
    }

    // date format: yyyy-MM-dd
    @RequestMapping(value = "/{id}/{date}/{day_type}/times", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getLineTimes(@PathVariable("id") long lineId,
                                                     @PathVariable("date") String dateFrom,
                                                     @PathVariable("day_type") int dayType) throws Exception
    {
        LocalDate dateFromLocalDate = getLocalDateFromString(dateFrom);
        Line line = getLineById(lineId);
        DayType dayTypeEnum = getDayTypeEnumFromInt(dayType);

        Schedule schedule = scheduleService.getScheduleByLineAndDateFromAndDayType(line, dateFromLocalDate, dayTypeEnum);
        if(schedule == null){
            throw new BadRequestException("There is no schedule with given params!");
        }

        List<String> times = scheduleService.getSortedStringTimes(schedule);
        return ResponseHelpers.getResponseData(times);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/{id}/{date}/{day_type}/{time}", method = RequestMethod.DELETE)
    public ResponseEntity removeTime(@PathVariable("id") long lineId,
                                     @PathVariable("date") String dateFrom,
                                     @PathVariable("day_type") int dayType,
                                     @PathVariable("time") String time) throws Exception
    {
        LocalDate dateFromLocalDate = getLocalDateFromString(dateFrom);
        Line line = getLineById(lineId);
        DayType dayTypeEnum = getDayTypeEnumFromInt(dayType);
        LocalTime localTime = getLocalTimeFromString(time);

        Schedule schedule = scheduleService.getScheduleByLineAndDateFromAndDayType(line, dateFromLocalDate, dayTypeEnum);
        if(schedule == null){
            throw new BadRequestException("There is no schedule with given params!");
        }

        List<MyLocalTime> times = schedule.getDepartureList();
        if(!times.removeIf(x -> x.getTime().equals(localTime))){
            throw new BadRequestException("Schedule doesn`t contain time!");
        }
        schedule.setDepartureList(times);

        Result addResult = scheduleService.save(schedule);
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        Result<Line> lineResult = lineService.findById(lineId);
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }
        Line lineRefreshed = lineResult.getData();
        boolean activityBefore = lineRefreshed.isActive();
        lineService.checkIfLineCanBeActive(lineRefreshed);
        boolean activityAfter = lineRefreshed.isActive();
        if(activityAfter != activityBefore) {
            lineService.checkIfLineCanBeActive(line);
            Result saveResult = lineService.save(line);
            if(saveResult.isFailure()) {
                throw new DatabaseException(saveResult.getMessage());
            }
        }

        return ResponseHelpers.getResponseData(addResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/{id}/{date}/{day_type}/{time}", method = RequestMethod.POST)
    public ResponseEntity addTime(@PathVariable("id") long lineId,
                                  @PathVariable("date") String dateFrom,
                                  @PathVariable("day_type") int dayType,
                                  @PathVariable("time") String time) throws Exception
    {
        LocalDate dateFromLocalDate = getLocalDateFromString(dateFrom);
        Line line = getLineById(lineId);
        DayType dayTypeEnum = getDayTypeEnumFromInt(dayType);
        LocalTime localTime = getLocalTimeFromString(time);

        Schedule schedule = scheduleService.getScheduleByLineAndDateFromAndDayType(line, dateFromLocalDate, dayTypeEnum);
        if(schedule == null){
            throw new BadRequestException("There is no schedule with given params!");
        }

        List<MyLocalTime> times = schedule.getDepartureList();
        if(times.stream().map(MyLocalTime::getTime).collect(Collectors.toList()).contains(localTime)){
            throw new BadRequestException("Schedule already contains time!");
        }

        times.add(new MyLocalTime(localTime));
        schedule.setDepartureList(times);

        Result addResult = scheduleService.save(schedule);
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        Result<Line> lineResult = lineService.findById(lineId);
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }
        Line lineRefreshed = lineResult.getData();
        boolean activityBefore = lineRefreshed.isActive();
        lineService.checkIfLineCanBeActive(lineRefreshed);
        boolean activityAfter = lineRefreshed.isActive();
        if(activityAfter != activityBefore){
            lineService.checkIfLineCanBeActive(line);
            Result saveResult = lineService.save(line);
            if(saveResult.isFailure()) {
                throw new DatabaseException(saveResult.getMessage());
            }
        }

        return ResponseHelpers.getResponseData(addResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveSchedule(@RequestBody ScheduleDTO scheduleDTO) throws Exception
    {
        LocalDate dateFromLocalDate = getLocalDateFromString(scheduleDTO.getDateFrom());
        Line line = getLineById(scheduleDTO.getLineId());

        List<Schedule> lineSchedules = line.getSchedules();
        for(int i=0; i<3; i++)
        {
            DayType dayTypeEnum = getDayTypeEnumFromInt(i);
            Schedule schedule = scheduleService.getScheduleByLineAndDateFromAndDayType(line, dateFromLocalDate, dayTypeEnum);
            if(schedule != null){
                throw new BadRequestException(String.format("There is already schedule for line %d date %s and daytype %s!",
                        scheduleDTO.getLineId(), scheduleDTO.getDateFrom(), dayTypeEnum.toString()));
            }

            List<LocalTime> localTimes = new ArrayList<>();
            for(String time : scheduleDTO.getTimes().get(i)){
                localTimes.add(getLocalTimeFromString(time));
            }
            List<MyLocalTime> myLocalTimes = localTimes.stream().map(MyLocalTime::new).collect(Collectors.toList());

            Schedule newSchedule = new Schedule(dayTypeEnum, dateFromLocalDate, myLocalTimes, line);
            lineSchedules.add(newSchedule);
        }

        line.setSchedules(lineSchedules);
        lineService.checkIfLineCanBeActive(line);

        Result saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    private LocalTime getLocalTimeFromString(String time) throws Exception
    {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(time, dateTimeFormatter);
        } catch (Exception e){
            throw new BadRequestException("Time format must be HH:mm");
        }
    }

    private Line getLineById(long lineId) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineId);
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }
        return  lineResult.getData();
    }

    private LocalDate getLocalDateFromString(String date) throws Exception
    {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, dateTimeFormatter);
        } catch (Exception e){
            throw new BadRequestException("Date format must be: yyyy-MM-dd");
        }
    }

    private DayType getDayTypeEnumFromInt(int dayType) throws Exception
    {
        try {
            return DayType.values()[dayType];
        }catch (IndexOutOfBoundsException e){
            throw new BadRequestException("Day type must be 0, 1 or 2 !");
        }
    }
}
