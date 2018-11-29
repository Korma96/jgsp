package com.mjvs.jgsp.repository;
import com.mjvs.jgsp.model.MyLocalTime;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.Schedule;

public interface ScheduleRepository extends Repository<Schedule, Long> {

    Schedule save(Schedule schedule);
    
    Schedule findByDepartureList(List<MyLocalTime> departureList);
}