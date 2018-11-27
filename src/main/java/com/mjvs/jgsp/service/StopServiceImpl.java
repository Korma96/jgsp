package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.StopRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StopServiceImpl extends BaseServiceImpl<Stop> implements StopService
{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private StopRepository stopRepository;

    @Autowired
    public StopServiceImpl(StopRepository stopRepository)
    {
        super(stopRepository);
        this.stopRepository = stopRepository;
    }

    @Override
    public Result<Boolean> exists(String name)
    {
        Stop stop = stopRepository.findByName(name);
        if(stop == null)
        {
            String message = Messages.AlreadyExists(StringConstants.Stop, name);
            logger.warn(message);
            return new Result<>(false, false, message);
        }
        return new Result<>(true);
    }

    @Override
    public Result<Stop> findByLatitudeAndLongitude(double latitude, double longitude)
    {
        Stop stop = stopRepository.findByLatitudeAndLongitude(latitude, longitude);
        if(stop == null) {
            String message = String.format("%s with lat %f and lng %f doesn`t exist!",
                    StringConstants.Stop, latitude, longitude);
            logger.warn(message);
            return new Result<>(null, message);
        }
        return new Result<>(stop, String.format("%s with lat %f and lng %f already exist!",
                StringConstants.Stop, latitude, longitude));
    }
}
