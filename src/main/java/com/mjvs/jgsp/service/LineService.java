package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.dto.TimesDTO;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;

import java.util.ArrayList;
import java.util.List;

public interface LineService extends ExtendedBaseService<Line>
{
    Result<List<BaseDTO>> getActiveLines();

    List<Line> getLines();

    List<Schedule> getLatestSchedules(List<Schedule> schedules);

    List<StopDTO> getSortedStopsById(List<Stop> stops);

	Line findByName(String string);

    ArrayList<TimesDTO> getDepartureLists(String dateStr, String dayStr, String[] lines);
    
    List<String> getLineNames();
}