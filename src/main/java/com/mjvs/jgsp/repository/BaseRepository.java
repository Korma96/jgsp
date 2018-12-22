package com.mjvs.jgsp.repository;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;


public interface BaseRepository<T>
{
    Optional<T> findById(Long id);

    List<T> findAll();

    List<T> findByDeleted(boolean deleted);

    T save(T t);

    void delete(T t);
}
