package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Stop;

public interface StopService extends BaseService<Stop>
{
    Result<Boolean> exists(String name);

    Result<Stop> findByLatitudeAndLongitude(double latitude, double longitude);
}
