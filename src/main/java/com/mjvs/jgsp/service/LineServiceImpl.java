package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.StringExtensions;
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
import java.util.HashMap;
import java.util.List;

@Service
public class LineServiceImpl implements LineService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private LineRepository lineRepository;

    @Autowired
    public LineServiceImpl(LineRepository lineRepository)
    {
        this.lineRepository = lineRepository;
    }

    @Override
    public Result<Boolean> delete(Line line)
    {
        try {
            lineRepository.delete(line);

            String message = Messages.SuccessfullyDeleted(StringConstants.Line, line.getName());
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorDeleting(StringConstants.Line, line.getName(), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, message);
        }
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
    public Result<Boolean> exists(Long id)
    {
        Line line = lineRepository.findById(id);
        if(line == null)
        {
            String message = Messages.AlreadyExists(StringConstants.Zone, id);
            logger.warn(message);
            return new Result<>(false, false, message);
        }
        return new Result<>(true);
    }

    @Override
    public Result<Line> findById(Long id)
    {
        Line line = lineRepository.findById(id);
        if(line == null)
        {
            String message = Messages.DoesNotExists(StringConstants.Line, id);
            logger.warn(message);
            return new Result<>(null, message);
        }
        return new Result<>(line);
    }

    @Override
    public Result<List<LineDTO>> getAll()
    {
        try {
            List<LineDTO> data = LineConverter
                    .ConvertLinesToLineDTOs(lineRepository.findAll());
            return new Result<>(data);
        }
        catch (Exception ex) {
            return new Result<>(null, false, ex.getMessage());
        }
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

    @Override
    public boolean rename(HashMap<String, String> data)
    {
        String oldName;
        String newName;

        try
        {
            oldName = data.get("oldName");
            newName = data.get("newName");
        }
        catch (Exception ex)
        {
            logger.error(String.format("Wrong data format! %s ", ex.getMessage()));
            return false;
        }

        Line line = lineRepository.findByName(oldName);
        if(line == null)
        {
            logger.warn(String.format("Line %s does not exist.", oldName));
            return false;
        }

        line.setName(newName);

        try
        {
            lineRepository.save(line);
            logger.info(String.format("Line %s successfully renamed to %s",
                    oldName, newName));
        }
        catch (Exception ex)
        {
            logger.info(String.format("Error renaming line %s to %s",
                    oldName, newName));
            return false;
        }

        return true;
    }

    @Override
    public Result<Boolean> save(Line line) throws Exception
    {
        if(line == null) {
            throw new Exception(Messages.CantBeNull(StringConstants.Line));
        }

        if(StringExtensions.isEmptyOrWhitespace(line.getName())) {
            throw new Exception(Messages.CantBeEmptyOrWhitespace(StringConstants.Line));
        }

        try {
            lineRepository.save(line);

            String message = Messages.SuccessfullyAdded(StringConstants.Line, line.getName());
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorAdding(StringConstants.Line, line.getName(), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, message);
        }
    }

	@Override
	public Line findByName(String name) {
		// TODO Auto-generated method stub
		return lineRepository.findByName(name);
	}

}
