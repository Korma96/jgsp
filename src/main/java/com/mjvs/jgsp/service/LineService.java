package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;

import java.util.List;

public interface LineService {

    boolean add(String lineName);

    boolean exists(String lineName);

    List<Line> getAll();

    Line getByName(String lineName);

    List<Line> getLinesByNames(List<String> lineNames);

    List<Stop> getLineStops(String lineName);

    List<Schedule> getSchedules(String lineName);

    boolean update(String oldLineName, String newLineName);

    boolean delete(String lineName);
}
