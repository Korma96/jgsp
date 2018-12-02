package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Line;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface LineRepository extends BaseRepository<Line>, Repository<Line, Long>
{
    List<Line> findByActive(boolean active);

    Line findByName(String name);
}
