package com.mjvs.jgsp.repository;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.Schedule;

public interface ScheduleRepository extends Repository<Schedule, Long> {

    Schedule save(Schedule schedule);
}
