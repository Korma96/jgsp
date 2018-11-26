package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;

import java.util.HashMap;
import java.util.List;

public interface LineService
{
    Result<Boolean> delete(Line line);

    Result<Boolean> exists(String name);

    Result<Boolean> exists(Long id);

    Result<Line> findById(Long id);
    
    Line findByName(String name);

    Result<List<LineDTO>> getAll();

    Result<List<LineDTO>> getActiveLines();

    List<Schedule> getLatestSchedules(List<Schedule> schedules);

    List<Stop> getSortedStopsById(List<Stop> stops);

    boolean rename(HashMap<String, String> data);

    Result<Boolean> save(Line line) throws Exception;
}
