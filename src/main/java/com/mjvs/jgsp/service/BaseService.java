package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;
import java.util.List;

public interface BaseService<T>
{
    Result<Boolean> delete(T obj);

    Result<Boolean> exists(Long id);

    Result<T> findById(Long id);

    Result<List<T>> getAll();

    Result<Boolean> save(T obj) throws Exception;
}
