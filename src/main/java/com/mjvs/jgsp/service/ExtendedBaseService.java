package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;

// PREREQUISITES: T must be class with name field !!!
public interface ExtendedBaseService<T> extends BaseService<T>
{
    Result<Boolean> exists(String name);
}
