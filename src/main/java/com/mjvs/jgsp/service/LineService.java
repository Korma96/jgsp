package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;

import java.util.HashMap;
import java.util.List;

public interface LineService {

    boolean add(String lineName, Long zoneId);

    Result<Boolean> exists(String name);

    Result<Boolean> exists(Long id);

    List<Line> getAll();

    List<Line> getActiveLines();

    List<Stop> getLineStops(Long lineId) throws LineNotFoundException;

    List<Schedule> getSchedules(Long lineId) throws LineNotFoundException;

    boolean delete(Long id) throws Exception;

    boolean rename(HashMap<String, String> data);

    Result<Line> findById(Long id);

    void save(Line line);
}
