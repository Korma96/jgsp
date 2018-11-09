package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.StopRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StopServiceImpl implements StopService
{
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private StopRepository stopRepository;

    @Override
    public boolean add(Stop newStop)
    {
        Optional<Stop> stop = stopRepository.findAll().stream()
                .filter(s -> s.getLatitude() == newStop.getLatitude()
                        && s.getLongitude() == newStop.getLongitude()).findFirst();
        if(stop.isPresent())
        {
            logger.debug(String.format("Stop %s %f %f already exists.",
                    newStop.getName(), newStop.getLatitude(), newStop.getLongitude()));
            return false;
        }

        try
        {
            stopRepository.save(newStop);
            logger.info("Stop %s %f %f successfully added!",
                    newStop.getName(), newStop.getLatitude(), newStop.getLongitude());
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new stop %f,%f message %s",
                    newStop.getLatitude(), newStop.getLongitude(), ex.getMessage()));
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Stop stop)
    {
        try
        {
            stopRepository.delete(stop);
            logger.info("Stop %s %f %f successfully deleted!",
                    stop.getName(), stop.getLatitude(), stop.getLongitude());
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error deleting stop %s %f %f message %s",
                    stop.getName(), stop.getLatitude(), stop.getLongitude(), ex.getMessage()));
            return  false;
        }

        return true;
    }

    @Override
    public List<Stop> getAll() {
        return stopRepository.findAll();
    }
}
