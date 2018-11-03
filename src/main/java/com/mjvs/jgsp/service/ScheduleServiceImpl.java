package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public void save(Schedule schedule) throws Exception {
        scheduleRepository.save(schedule);
    }
}
