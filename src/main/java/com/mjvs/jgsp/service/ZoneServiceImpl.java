package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneServiceImpl implements ZoneService
{
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    ZoneRepository zoneRepository;

    
    @Override
    public boolean add(String zoneName) {
    	return addZoneWithLines(zoneName, null);
    }
    
    @Override
    public boolean addZoneWithLines(String zoneName, List<String> lineNames) {
        if(exists(zoneName))
        {
            logger.debug(String.format("Zone %s already exists.", zoneName));
            return false;
        }
        
        StringBuilder sbMessage = new StringBuilder("");

        try
        {
        	Zone zone = new Zone(zoneName);
        	if(lineNames != null) {
	        	List<Line> lines = lineNames.stream()
	        			.map(lineName -> new Line(lineName, zone))
	        			.collect(Collectors.toList());
	        	zone.setLines(lines);
	        	
	        	lineNames.stream()
    					.forEach(lineName -> {sbMessage.append(","); sbMessage.append(lineName);} );
        	}
        	else sbMessage.append("null");
        	
            zoneRepository.save(zone);
            logger.info(String.format("Zone %s with lines (%s) successfully added!", zoneName, sbMessage.substring(1)));
        }
        catch (Exception ex)
        {	
            logger.error(String.format("Error adding new zone %s with lines (%s) message %s",zoneName, sbMessage.substring(1), ex.getMessage()));
            return false;
        }

        return true;
    }

    public void save(Zone zone) {
        zoneRepository.save(zone);
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
