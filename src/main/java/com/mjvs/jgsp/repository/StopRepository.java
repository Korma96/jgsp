package com.mjvs.jgsp.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

public interface StopRepository extends BaseRepository<Stop>, Repository<Stop, Long>
{
    Stop findByLatitudeAndLongitude(double latitude, double longitude);

    Stop findByName(String name);
}
