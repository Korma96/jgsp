package com.mjvs.jgsp.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.Stop;

public interface StopRepository extends Repository<Stop, Long>
{
    Stop findByLatitudeAndLongitude(double latitude, double longitude);

    List<Stop> findAll();

    Stop save(Stop stop);

    void delete(Stop stop);
}
