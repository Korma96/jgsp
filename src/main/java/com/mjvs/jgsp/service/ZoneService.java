package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.Zone;

// BaseService<T> must be extended, it can`t be used directly
public interface ZoneService extends BaseService<Zone>
{
    Result<Boolean> exists(String name);
}
