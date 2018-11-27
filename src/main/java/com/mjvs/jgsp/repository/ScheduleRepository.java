package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Schedule;
import org.springframework.data.repository.Repository;

public interface ScheduleRepository extends BaseRepository<Schedule>, Repository<Schedule, Long>
{

}
