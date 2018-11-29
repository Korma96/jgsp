package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.MyLocalTime;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.repository.ScheduleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

	@Override
	public Schedule findByDepartureList(List<MyLocalTime> departureList) {
		return scheduleRepository.findByDepartureList(departureList);
	}
}
