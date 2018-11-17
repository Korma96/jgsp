package com.mjvs.jgsp.service;

import com.mjvs.jgsp.exceptions.LineNotFoundException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.LineRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        if(exists(lineName))
        {
            logger.debug(String.format("Line %s already exists.", lineName));
            return false;
        }

        try
        {
            Zone zone = zoneService.getZone(zoneId);
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

        return true;
    }

    @Override
    public void save(Line line, Zone zone) {
        line = lineRepository.save(line);
        zone.getLines().add(line);
        zoneService.save(zone);
        logger.info(String.format("Line %s successfully added!", line.getName()));
    }

    @Override
    public List<Line> getLines()
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
    public boolean update(String oldLineName, String newLineName) {
        return false;
    }

    @Override
    public void delete(Long lineId) throws Exception
    {
        Line line = lineRepository.findById(lineId);
        if(line == null){
            String message = String.format("Line with id %d does not exist.", lineId);
            logger.error(message);
            throw new LineNotFoundException(message);
        }

        lineRepository.delete(line);
    }

    @Override
    public Line getLine(Long lineId)
    {
        return  lineRepository.findById(lineId);
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
    public boolean exists(String lineName)
    {
        Line line = lineRepository.findByName(lineName);
        return line != null;
    }
}
