package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.StopRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            logger.debug(String.format("Stop %f,%f already exists.",
                    newStop.getLatitude(), newStop.getLongitude()));
            return false;
        }

        try
        {
            stopRepository.save(newStop);
            return true;
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new stop %f,%f! Message: %s",
                    newStop.getLatitude(), newStop.getLongitude(), ex.getMessage()));
        }

        return false;
    }

    @Override
    public boolean delete(Stop stop) {
        return false;
    }
}
