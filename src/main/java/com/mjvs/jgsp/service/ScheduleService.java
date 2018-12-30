package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Schedule;

import java.util.List;

public interface ScheduleService extends BaseService<Schedule>
{

    List<String> getAllDatesFrom();
}