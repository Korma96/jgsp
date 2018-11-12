package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;

import java.util.List;

public class LineDTO {
    private String name;
    private List<Stop> stops;

    public LineDTO() {

    }

    public LineDTO(String name, List<Stop> stops) {
        this.name = name;
        this.stops = stops;
    }

    public LineDTO(Line line) {
        this(line.getName(), line.getStops());
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
}
