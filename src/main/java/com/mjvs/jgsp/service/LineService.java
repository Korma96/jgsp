package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;

import java.util.List;

public interface LineService extends BaseService<Line>
{
    Result<Boolean> exists(String name);

    Result<List<LineDTO>> getActiveLines();

    List<Schedule> getLatestSchedules(List<Schedule> schedules);

    List<Stop> getSortedStopsById(List<Stop> stops);

	Line findByName(String string);
}