package com.mjvs.jgsp.repository;

import java.util.List;
import java.util.Optional;


public interface BaseRepository<T>
{
    Optional<T> findById(Long id);

    List<T> findAll();

    T save(T line);

    void delete(T line);
}
