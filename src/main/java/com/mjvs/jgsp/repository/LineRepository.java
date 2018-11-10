package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Line;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface LineRepository extends Repository<Line, Long>
{
    Line findByName(String name);

    List<Line> findAll();

    Line save(Line line);

    void delete(Line line);
}
