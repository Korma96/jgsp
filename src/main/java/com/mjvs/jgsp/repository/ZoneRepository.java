package com.mjvs.jgsp.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.Zone;

public interface ZoneRepository extends Repository<Zone, Long>
{
    Zone findById(Long id);

    Zone findByName(String zoneName);

    List<Zone> findAll();

    Zone save(Zone zone);

    void delete(Zone zone);
}
