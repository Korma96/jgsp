package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Stop> stops;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transport> transports;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    public Line(String name, List<Stop> stops, List<Transport> transports, List<Schedule> schedules) {
        this.name = name;
        this.stops = stops;
        this.transports = transports;
        this.schedules = schedules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
