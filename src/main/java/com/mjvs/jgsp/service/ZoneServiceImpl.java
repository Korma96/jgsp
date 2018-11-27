package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// BaseServiceImpl<T> must be extended, it can`t be used directly or by dependency injection
@Service
public class ZoneServiceImpl extends BaseServiceImpl<Zone> implements ZoneService
{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private ZoneRepository zoneRepository;

    @Autowired
    public ZoneServiceImpl(ZoneRepository zoneRepository)
    {
        super(zoneRepository);
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Result<Boolean> exists(String name)
    {
        Zone zone = zoneRepository.findByName(name);
        if(zone != null)
        {
            String message = Messages.AlreadyExists(StringConstants.Stop, name);
            logger.warn(message);
            return new Result<>(true, false, message);
        }
        return new Result<>(false);
    }
}
