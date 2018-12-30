package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl extends BaseServiceImpl<Schedule> implements ScheduleService
{
    private ScheduleRepository scheduleRepository;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository)
    {
        super(scheduleRepository);
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<String> getAllDatesFrom() {
        List<Schedule> schedules = scheduleRepository.findAll();

        List<String> dates = schedules.stream()
                                .map(s -> s.getDateFrom().format(dateTimeFormatter))
                                .distinct()
                                .collect(Collectors.toList());

        return dates;
    }
}