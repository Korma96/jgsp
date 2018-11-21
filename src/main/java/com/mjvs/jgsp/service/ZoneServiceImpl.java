package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.ZoneLiteDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.StringExtensions;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.LineRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
import com.mjvs.jgsp.helpers.converter.ZoneConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService
{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private ZoneRepository zoneRepository;
    private LineRepository lineRepository;

    @Autowired
    public ZoneServiceImpl(ZoneRepository zoneRepository, LineRepository lineRepository)
    {
        this.zoneRepository = zoneRepository;
        this.lineRepository = lineRepository;
    }


    @Override
    public Result<Boolean> delete(Zone zone)
    {
        try {
            zoneRepository.delete(zone);

            String message = Messages.SuccessfullyDeleted(StringConstants.Zone, zone.getName());
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorDeleting(StringConstants.Zone, zone.getName(), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, message);
        }
    }

    @Override
    public Result<Boolean> exists(String name)
    {
        Zone zone = zoneRepository.findByName(name);
        if(zone != null)
        {
            String message = Messages.AlreadyExists(StringConstants.Zone, name);
            logger.warn(message);
            return new Result<>(true, true, message);
        }
        return new Result<>(false);
    }

    @Override
    public Result<Boolean> exists(Long id)
    {
        Zone zone = zoneRepository.findById(id);
        if(zone != null)
        {
            String message = Messages.AlreadyExists(StringConstants.Zone, id);
            logger.warn(message);
            return new Result<>(true, message);
        }
        return new Result<>(false);
    }

    @Override
    public Result<Zone> findById(Long id)
    {
        Zone zone = zoneRepository.findById(id);
        if(zone == null) {
            String message = Messages.DoesNotExists(StringConstants.Zone, id);
            logger.warn(message);
            return new Result<>(null, message);
        }
        return new Result<>(zone);
    }

    @Override
    public Result<List<ZoneLiteDTO>> getAll()
    {
        try {
            List<ZoneLiteDTO> data = ZoneConverter.ConvertZonesToZoneLiteDTOs(zoneRepository.findAll());
            return new Result<>(data);
        }
        catch (Exception ex) {
            return new Result<>(null, false, ex.getMessage());
        }
    }

    @Override
    public Result<Boolean> save(Zone zone) throws Exception
    {
        if(zone == null) {
            throw new Exception(Messages.CantBeNull(StringConstants.Zone));
        }

        if(StringExtensions.isEmptyOrWhitespace(zone.getName())) {
            throw new Exception(Messages.CantBeEmptyOrWhitespace(StringConstants.Zone));
        }

        try {
            zoneRepository.save(zone);

            String message = Messages.SuccessfullyAdded(StringConstants.Zone, zone.getName());
            logger.info(message);
            return new Result<>(true, message);
        }
        catch (Exception ex)
        {
            String message = Messages.ErrorAdding(StringConstants.Zone, zone.getName(), ex.getMessage());
            logger.error(message);
            return new Result<>(false, false, message);
        }
    }
}
