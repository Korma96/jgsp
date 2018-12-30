package com.mjvs.jgsp.repository;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.Schedule;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ScheduleRepository extends BaseRepository<Schedule>, Repository<Schedule, Long>
{

    //List<Schedule> findDistinctByDateFrom();
}