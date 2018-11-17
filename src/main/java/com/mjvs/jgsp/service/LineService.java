package com.mjvs.jgsp.service;

import com.mjvs.jgsp.exceptions.LineNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.model.Zone;

import java.util.List;

public interface LineService {

    boolean add(String lineName, Long zoneId);

    void save(Line line, Zone zone);

    boolean exists(String lineName);

    List<Line> getLines();

    List<Line> getActiveLines();

    Line getLine(Long lineId);

    //List<Line> getLinesByNames(List<String> lineNames);

    List<Stop> getLineStops(Long lineId)  throws LineNotFoundException;

    List<Schedule> getSchedules(Long lineId) throws LineNotFoundException;

    boolean update(String oldLineName, String newLineName);

    void delete(Long lineId) throws Exception;
}
