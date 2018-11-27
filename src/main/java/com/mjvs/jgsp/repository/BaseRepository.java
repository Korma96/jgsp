package com.mjvs.jgsp.repository;

import java.util.List;


public interface BaseRepository<T>
{
    T findById(Long id);

    List<T> findAll();

    T save(T line);

    void delete(T line);
}
