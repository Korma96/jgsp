package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class LineServiceImpl implements LineService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private ZoneService zoneService;

    @Override
    public boolean add(String lineName, Long zoneId)
    {
        /*
        if(exists(lineName))
        {
            logger.debug(String.format("Line %s already exists.", lineName));
            return false;
        }

        try
        {
            Zone zone = zoneService.findById(zoneId);
            if(zone == null) {
                logger.info(String.format("When adding a new line (%s), the zone with id %d was not found!", lineName, zoneId));
                return false;
            }

           Line line = new Line(lineName, zone);
            save(line, zone);
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new line %s message %s",
                    lineName, ex.getMessage()));
            return false;
        }
        */
        return true;
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
    public List<Line> getAll()
    {
        return lineRepository.findAll();
    }

    @Override
    public List<Line> getActiveLines() { return lineRepository.findByActive(true); }

    @Override
    public List<Stop> getLineStops(Long lineId) throws LineNotFoundException
    {
        Line line = lineRepository.findById(lineId);
        if(line == null)
        {
            String message = String.format("Line with id %d does not exist.", lineId);
            logger.error(message);
            throw new LineNotFoundException(message);
        }

        List<Stop> stops = line.getStops();
        stops.sort(new Comparator<Stop>() { // sortiramo jer u dokumentaciji nije grantovano
                                            // da cemo dobiti sortirane objekte po id
            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        return stops;
    }

    @Override
    public boolean delete(Long lineId) throws Exception
    {
        Line line = lineRepository.findById(lineId);
        if(line == null){
            String message = String.format("Line with id %d does not exist.", lineId);
            logger.error(message);
            throw new LineNotFoundException(message);
        }

        lineRepository.delete(line);
        return true;
    }

    @Override
    public List<Schedule> getSchedules(Long lineId) throws LineNotFoundException
    {
        Line line = lineRepository.findById(lineId);
        if(line == null)
        {
            String message = String.format("Line with id %d does not exist.", lineId);
            logger.error(message);
            throw new LineNotFoundException(message);
        }

        return line.getSchedules();
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
    public void save(Line line)
    {
        lineRepository.save(line);
    }



}
