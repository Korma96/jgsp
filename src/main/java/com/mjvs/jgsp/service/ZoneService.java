package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.ZoneLiteDTO;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Zone;

import java.util.HashMap;
import java.util.List;

public interface ZoneService
{
    Result<Boolean> exists(String name);

    Result<Boolean> exists(Long id);

    Result<Zone> findById(Long id);

    Result<List<ZoneLiteDTO>> getAll();

    Result<Boolean> delete(Zone zone);

    Result<Boolean> removeLineFromZone(String zoneName, String lineName);

    Result<Boolean> rename(HashMap<String, String> data);

    Result<Boolean> save(Zone zone) throws Exception;
}
