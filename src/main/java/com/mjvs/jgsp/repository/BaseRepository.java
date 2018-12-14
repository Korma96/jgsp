package com.mjvs.jgsp.repository;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface BaseRepository<T>
{
    T findById(Long id);

    List<T> findAll();

    List<T> findByDeleted(boolean deleted);

    T save(T t);

    void delete(T t);
}
