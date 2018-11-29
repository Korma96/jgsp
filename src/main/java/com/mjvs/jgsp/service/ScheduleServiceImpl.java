package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl extends BaseServiceImpl<Schedule> implements ScheduleService
{
    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository)
    {
        super(scheduleRepository);
    }
}