package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;

import java.util.HashMap;
import java.util.List;

public interface LineService {

    boolean add(String lineName);

    Result<Boolean> exists(String name);

    Result<Boolean> exists(Long id);

    List<Line> getAll();

    Line getByName(String lineName);

    List<Stop> getLineStops(String lineName);

    List<Schedule> getSchedules(String lineName);

    boolean delete(String lineName);

    boolean rename(HashMap<String, String> data);

    Result<Line> findById(Long id);
}
