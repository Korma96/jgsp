package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.Result;

import java.util.List;

public interface BaseService<T>
{
    Result<Boolean> delete(T obj) throws Exception;

    Result<Boolean> exists(Long id) throws Exception;

    Result<T> findById(Long id) throws Exception;

    Result<List<T>> getAll();

    Result<List<T>> getAllUndeletd();

    Result<Boolean> save(T obj) throws Exception;
}
