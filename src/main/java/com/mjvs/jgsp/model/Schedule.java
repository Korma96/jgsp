package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name="day_type", unique=false, nullable=false)
    private DayType dayType;

    @Column(name="date_from", unique=false, nullable=false)
    private LocalDate dateFrom;

    @Column(name="departure_list", unique=false, nullable=false)
    private LocalTime[] departureList;

    public Schedule(DayType dayType, LocalDate dateFrom, LocalTime[] departureList) {
        this.dayType = dayType;
        this.dateFrom = dateFrom;
        this.departureList = departureList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalTime[] getDepartureList() {
        return departureList;
    }

    public void setDepartureList(LocalTime[] departureList) {
        this.departureList = departureList;
    }
}
