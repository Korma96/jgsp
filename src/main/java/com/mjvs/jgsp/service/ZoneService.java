package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Zone;

import java.util.HashMap;
import java.util.List;

public interface ZoneService
{
    boolean add(String zoneName);

    boolean exists(String zoneName);

    List<Zone> getAll();

    boolean delete(String zoneName);

    boolean addLineToZone(String zoneName, String lineName);

    boolean removeLineFromZone(String zoneName, String lineName);

    boolean rename(HashMap<String, String> data);
}
