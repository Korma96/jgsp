package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Zone;
import org.springframework.data.repository.Repository;

// BaseRepository<T> must be extended, it can`t be used directly
public interface ZoneRepository extends BaseRepository<Zone>, Repository<Zone, Long>
{
    Zone findByName(String name);
}
