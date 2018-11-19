package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Zone;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ZoneRepository extends Repository<Zone, Long>
{
    Zone findById(Long id);

    Zone findByName(String zoneName);

    List<Zone> findAll();

    Zone save(Zone zone);

    void delete(Zone zone);
}
