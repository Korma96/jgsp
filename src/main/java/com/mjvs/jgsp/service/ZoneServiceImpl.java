package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.LineRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// BaseServiceImpl<T> must be extended, it can`t be used directly or by dependency injection
@Service
public class ZoneServiceImpl extends ExtendedBaseServiceImpl<Zone> implements ZoneService
{
    private ZoneRepository zoneRepository;
    @Autowired
    public ZoneServiceImpl(ZoneRepository zoneRepository)
    {
        super(zoneRepository);

        this.zoneRepository = zoneRepository;
    }

    public Zone findByName(String name) {
        return this.zoneRepository.findByNameAndDeleted(name, false);
    }
}
