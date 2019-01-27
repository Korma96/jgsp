package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

public interface ScheduleRepository extends BaseRepository<Schedule>, Repository<Schedule, Long>
{
    Schedule findTopByDateFromAndLineAndDayType(LocalDate dateFrom, Line line, DayType dayType);
    //List<Schedule> findDistinctByDateFrom();
}