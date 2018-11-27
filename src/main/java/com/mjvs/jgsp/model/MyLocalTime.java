package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
public class MyLocalTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "time", unique = false, nullable = false)
    private LocalTime time;

    public MyLocalTime() {

    }

    public MyLocalTime(LocalTime time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
