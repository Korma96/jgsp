package com.mjvs.jgsp.model;

import javax.persistence.*;

@Entity
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "x_coordinate", unique = false, nullable = false)
    private double xCoordinate;

    @Column(name = "y_coordinate", unique = false, nullable = false)
    private double yCoordinate;

    @Column(name = "name", unique = false, nullable = false)
    private String name;


    public Stop(double xCoordinate, double yCoordinate, String name) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return xCoordinate + "|" + yCoordinate + "|" + name;
    }
}
