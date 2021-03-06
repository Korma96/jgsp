package com.mjvs.jgsp.repository;

// PREREQUISITES: T must be class with name field !!!
public interface ExtendedBaseRepository<T> extends BaseRepository<T>
{
    T findByNameAndDeleted(String name, boolean deleted);
}
