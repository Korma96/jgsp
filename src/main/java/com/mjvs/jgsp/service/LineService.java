package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;

import java.util.List;

public interface LineService {

    boolean add(Line line);

    List<Line> getAll();

    List<Stop> getLineStops(String lineName);

    boolean update(String oldLineName, String newLineName);

    boolean delete(String lineName);
}
