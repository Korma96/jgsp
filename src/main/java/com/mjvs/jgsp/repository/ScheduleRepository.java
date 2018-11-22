package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Schedule save(Schedule schedule);
}
