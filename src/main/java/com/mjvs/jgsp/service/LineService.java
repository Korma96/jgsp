package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.model.Zone;

import java.util.HashMap;
import java.util.List;

public interface LineService {

    boolean add(String lineName, Long zoneId);

    Result<Boolean> exists(String name);

    Result<Boolean> exists(Long id);

    List<Line> getAll();

    List<Line> getActiveLines();

    List<Stop> getLineStops(String lineName);

    List<Schedule> getSchedules(Long lineId) throws LineNotFoundException;

    boolean delete(String lineName);

    boolean rename(HashMap<String, String> data);

    Result<Line> findById(Long id);
}
