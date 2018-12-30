package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Point;
import com.mjvs.jgsp.model.Stop;
import org.springframework.data.repository.Repository;

public interface PointRepository extends BaseRepository<Point>, Repository<Point, Long>
{
    Point findByLatitudeAndLongitude(double latitude, double longitude);
}
