package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "latitude", unique = false, nullable = false)
    private double latitude;

    @Column(name = "longitude", unique = false, nullable = false)
    private double longitude;

    @Column(name = "name", unique = false, nullable = false)
    private String name;

    public Stop() {

    }

    public Stop(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
		this.name = name;
    }

    public Stop()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return latitude + "|" + longitude + "|" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(id, stop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
