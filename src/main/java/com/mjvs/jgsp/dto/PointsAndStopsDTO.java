package com.mjvs.jgsp.dto;

import java.util.List;

public class PointsAndStopsDTO {
    private List<PointDTO> points;
    private List<StopDTO> stops;

    public PointsAndStopsDTO() {

    }

    public PointsAndStopsDTO(List<PointDTO> points, List<StopDTO> stops) {
        this.points = points;
        this.stops = stops;
    }

    public List<PointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<PointDTO> points) {
        this.points = points;
    }

    public List<StopDTO> getStops() {
        return stops;
    }

    public void setStops(List<StopDTO> stops) {
        this.stops = stops;
    }
}
