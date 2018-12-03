package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Line;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface LineRepository extends ExtendedBaseRepository<Line>, Repository<Line, Long>
{
    List<Line> findByActive(boolean active);

    Line findByName(String name);
}
