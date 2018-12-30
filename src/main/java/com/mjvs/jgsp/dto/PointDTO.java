package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Point;

import javax.persistence.Column;

public class PointDTO {
    private double lat;
    private double lng;

    public PointDTO() {

    }

    public PointDTO(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public PointDTO(Point point) {
        this.lat = point.getLatitude();
        this.lng = point.getLongitude();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
