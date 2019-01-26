package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService extends BaseService<Schedule>
{
    List<String> getLineDatesFrom(Line line);
    List<String> getAllDatesFrom();
    Schedule getScheduleByLineAndDateFromAndDayType(Line line, LocalDate dateFromLocalDate, DayType dayTypeEnum);
    List<String> getSortedStringTimes(Schedule schedule);
}