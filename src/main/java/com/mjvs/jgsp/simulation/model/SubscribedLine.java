package com.mjvs.jgsp.simulation.model;

import com.mjvs.jgsp.model.Line;

import java.util.Random;

public class SubscribedLine {
    private int numOfSubscribers;
    private Line line;
    private int indexOfStopForFirstVehicle;
    private int numOfVehicles;

    private static Random random = new Random();

    public SubscribedLine() {

    }

    public SubscribedLine(Line line, int numOfVehicles) {
        this.numOfSubscribers = 1;
        this.line = line;
        this.indexOfStopForFirstVehicle =  Math.abs(random.nextInt()) % line.getStops().size();
        this.numOfVehicles = numOfVehicles;
    }

    public int getNumOfSubscribers() {
        return numOfSubscribers;
    }

    public void setNumOfSubscribers(int numOfSubscribers) {
        this.numOfSubscribers = numOfSubscribers;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getIndexOfStopForFirstVehicle() {
        return indexOfStopForFirstVehicle;
    }

    public void setIndexOfStopForFirstVehicle(int indexOfStopForFirstVehicle) {
        this.indexOfStopForFirstVehicle = indexOfStopForFirstVehicle;
    }

    public int getNumOfVehicles() {
        return numOfVehicles;
    }

    public void setNumOfVehicles(int numOfVehicles) {
        this.numOfVehicles = numOfVehicles;
    }
}
