package com.mjvs.jgsp.service;

import java.util.List;

import com.mjvs.jgsp.model.MyLocalTime;
import com.mjvs.jgsp.model.Schedule;

public interface ScheduleService {

    Schedule save(Schedule schedule) throws Exception;

	Schedule findByDepartureList(List<MyLocalTime> departureList);
}
