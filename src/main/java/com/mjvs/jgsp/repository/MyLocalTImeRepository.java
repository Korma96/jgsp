package com.mjvs.jgsp.repository;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mjvs.jgsp.model.MyLocalTime;


public interface MyLocalTImeRepository extends JpaRepository<MyLocalTime, Long> {
	
    MyLocalTime findByTime(LocalTime time);
    
    MyLocalTime save (MyLocalTime myLocalTime);

}
