package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Zone;

import java.util.List;

public interface ZoneService
{
    boolean add(String zoneName);

    void save(Zone zone);

    boolean exists(String zoneName);

    List<Zone> getAll();

    boolean delete(String zoneName);

    boolean addLinesToZone(String zoneName, List<String> lineNames);

    Zone getZone(Long zoneId);
}
