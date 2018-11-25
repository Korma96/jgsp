package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.StopRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class StopServiceImpl implements StopService
{
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private StopRepository stopRepository;

    @Override
    public boolean add(Stop newStop)
    {
        Stop stop = stopRepository.findByLatitudeAndLongitude(
                newStop.getLatitude(), newStop.getLongitude());
        if(stop != null)
        {
            logger.debug(String.format("Stop %f, %f already exists.",
                    newStop.getLatitude(), newStop.getLongitude()));
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
            logger.error(String.format("Error adding new stop %s %f,%f message %s",
                    newStop.getName(), newStop.getLatitude(), newStop.getLongitude(), ex.getMessage()));
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(double latitude, double longitude)
    {
        Stop stop = stopRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(stop == null){
            logger.warn(String.format("Stop %f, %f does not exist.", latitude, longitude));
            return false;
        }

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

    @Override
    public boolean rename(HashMap<String, String> data)
    {
        String oldName;
        String newName;
        double longitude;
        double latitude;
        try
        {
            oldName = data.get("oldName");
            newName = data.get("newName");
            longitude = new Double(data.get("longitude"));
            latitude = new Double(data.get("latitude"));
        }
        catch (Exception ex)
        {
            logger.error(String.format("Wrong data format! %s", ex.getMessage()));
            return false;
        }

        Stop stop = stopRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(stop == null)
        {
            logger.warn(String.format("Stop %f, %f does not exist.", latitude, longitude));
            return false;
        }

        stop.setName(newName);

        try
        {
            stopRepository.save(stop);
            logger.info(String.format("Stop %s successfully renamed to %s",
                    oldName, newName));
        }
        catch (Exception ex)
        {
            logger.info(String.format("Error renaming stop %s to %s",
                    oldName, newName));
            return false;
        }

        return true;
    }

    @Override
    public boolean changeCoordinates(HashMap<String, Double> data)
    {
        double oldLat;
        double oldLng;
        double newLat;
        double newLng;

        try
        {
            oldLat = data.get("oldLat");
            oldLng = data.get("oldLng");
            newLat = data.get("newLat");
            newLng = data.get("newLng");
        }
        catch (Exception ex)
        {
            logger.error(String.format("Wrong data format! %s", ex.getMessage()));
            return false;
        }

        Stop stop = stopRepository.findByLatitudeAndLongitude(oldLat, oldLng);
        if(stop == null)
        {
            logger.warn(String.format("Stop %f, %f does not exist.", oldLat, oldLng));
            return false;
        }

        stop.setLatitude(newLat);
        stop.setLongitude(newLng);

        try
        {
            stopRepository.save(stop);
            logger.info(String.format("Stop %f, %f successfully changed to %f, %f",
                    oldLat, oldLng, newLat, newLng));
        }
        catch (Exception ex)
        {
            logger.info(String.format("Error changing stop %f, %f to %f, %f",
                    oldLat, oldLng, newLat, newLng));
            return false;
        }

        return true;
    }
}
