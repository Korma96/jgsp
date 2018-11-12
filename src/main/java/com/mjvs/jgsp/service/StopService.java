package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Stop;

import java.util.List;

public interface StopService
{
    boolean add(Stop stop);

    boolean delete(Stop stop);

    List<Stop> getAll();
}
