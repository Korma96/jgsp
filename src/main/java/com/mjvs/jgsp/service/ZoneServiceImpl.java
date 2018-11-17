package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService
{
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    ZoneRepository zoneRepository;

    @Override
    public boolean add(String zoneName) {
        if(exists(zoneName))
        {
            logger.debug(String.format("Zone %s already exists.", zoneName));
            return false;
        }

        try
        {
            zoneRepository.save(new Zone(zoneName));
            logger.info(String.format("Zone %s successfully added!", zoneName));
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new zone %s message %s",
                    zoneName, ex.getMessage()));
            return false;
        }

        return true;
    }

    public void save(Zone zone) {
        zoneRepository.save(zone);
    }

    @Override
    public boolean addLinesToZone(String zoneName, List<String> lines)
    {
        return false;
    }

    @Override
    public Zone getZone(Long zoneId) {
        return zoneRepository.findById(zoneId);
    }

    @Override
    public List<Zone> getAll() {
        return zoneRepository.findAll();
    }

    @Override
    public boolean delete(String zoneName)
    {
        Zone zone = zoneRepository.findByName(zoneName);
        if(zone == null)
        {
            logger.warn(String.format("Zone %s does not exist!", zoneName));
            return false;
        }

        try
        {
            zoneRepository.delete(zone);
            logger.info("Zone %s successfully deleted!", zoneName);
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error deleting zone %s message %s",
                    zoneName, ex.getMessage()));
            return false;
        }

        return true;
    }

    @Override
    public boolean exists(String zoneName)
    {
        Zone zone = zoneRepository.findByName(zoneName);
        return zone != null;
    }
}
