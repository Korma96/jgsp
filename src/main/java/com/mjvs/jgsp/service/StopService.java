package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Stop;

import java.util.HashMap;
import java.util.List;

public interface StopService
{
    boolean add(Stop stop);

    boolean delete(double latitude, double longitude);

    List<Stop> getAll();

    boolean rename(HashMap<String, String> data);

    boolean changeCoordinates(HashMap<String, Double> data);
}
