package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LineServiceImpl extends BaseServiceImpl<Line> implements LineService
{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private LineRepository lineRepository;

    @Autowired
    public LineServiceImpl(LineRepository lineRepository)
    {
        super(lineRepository);
        this.lineRepository = lineRepository;
    }

    @Override
    public Result<Boolean> exists(String name)
    {
        Line line = lineRepository.findByName(name);
        if(line == null)
        {
            String message = Messages.AlreadyExists(StringConstants.Line, name);
            logger.warn(message);
            return new Result<>(false, false, message);
        }
        return new Result<>(true);
    }

    @Override
    public Result<List<LineDTO>> getActiveLines()
    {
        try {
            List<LineDTO> data = LineConverter
                    .ConvertLinesToLineDTOs(lineRepository.findByActive(true));
            return new Result<>(data);
        }
        catch (Exception ex) {
            return new Result<>(null, false, ex.getMessage());
        }
    }

    @Override
    public List<Stop> getSortedStopsById(List<Stop> stops)
    {
        stops.sort(new Comparator<Stop>() { // sortiramo jer u dokumentaciji nije garantovano
            // da cemo dobiti sortirane objekte po id
            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        return stops;
    }

    @Override
    public List<Schedule> getLatestSchedules(List<Schedule> schedules)
    {
        if(schedules.size() == 0) {
            return new ArrayList<>();
        }

        List<Schedule> latestSchedules = new ArrayList<>();
        LocalDate latestDate = schedules.stream().findFirst().get().getDateFrom();

        for(Schedule schedule : schedules)
        {
            if(schedule.getDateFrom().isBefore(latestDate))
            {
                continue;
            }
            else if(schedule.getDateFrom().isAfter(latestDate))
            {
                latestDate = schedule.getDateFrom();
                latestSchedules = new ArrayList<>();
                latestSchedules.add(schedule);
            }
            else
            {
                latestSchedules.add(schedule);
            }
        }

        return latestSchedules;
    }

}
