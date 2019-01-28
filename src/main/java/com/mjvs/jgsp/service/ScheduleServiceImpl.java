package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
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
    public List<String> getLineDatesFrom(Line line)
    {
        List<LocalDate> datesFrom = line.getSchedules().stream().map(Schedule::getDateFrom)
                .sorted(new Comparator<LocalDate>(){
                    @Override
                    public int compare(LocalDate o1, LocalDate o2) {
                        return o1.compareTo(o2);
                    }
                }).collect(Collectors.toList());

        return datesFrom.stream().map(LocalDate::toString).distinct().collect(Collectors.toList());
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

    @Override
    public Schedule getScheduleByLineAndDateFromAndDayType(Line line, LocalDate dateFromLocalDate, DayType dayTypeEnum)
    {
        return scheduleRepository.findTopByDateFromAndLineAndDayType(dateFromLocalDate, line, dayTypeEnum);
    }

    @Override
    public List<String> getSortedStringTimes(Schedule schedule) {
        List<String> times = schedule.getDepartureList().stream()
                                .map(x -> x.getTime().toString()).collect(Collectors.toList());
        Collections.sort(times);
        return times;
    }
}