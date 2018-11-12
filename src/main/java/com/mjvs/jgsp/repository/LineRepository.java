package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

    List<Line> findAll();
}
