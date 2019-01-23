package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Zone;
import org.springframework.data.repository.Repository;

// BaseRepository<T> must be extended, it can`t be used directly
public interface ZoneRepository extends ExtendedBaseRepository<Zone>, Repository<Zone, Long>
{
    Zone findById(long id);

    Zone findByName(String name);
}
