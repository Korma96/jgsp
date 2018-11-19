package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Stop;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface StopRepository extends Repository<Stop, Long>
{
    Stop findByLatitudeAndLongitude(double latitude, double longitude);

    List<Stop> findAll();

    Stop save(Stop stop);

    void delete(Stop stop);
}
