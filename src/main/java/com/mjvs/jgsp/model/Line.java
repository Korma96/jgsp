package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Line extends LineZone {
    @Column(name = "name", unique = false, nullable = false)
    private String name;

    @Column(name = "active", unique = false, nullable = false)
    private boolean active;

    @Column(name = "minutes_required_for_whole_route", unique = false, nullable = false)
    private int minutesRequiredForWholeRoute;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Zone zone;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Point> points;

    @ManyToMany(fetch = FetchType.EAGER/*fetch = FetchType.LAZY*/, cascade = CascadeType.ALL)
    private List<Stop> stops;

    // orphanRemoval znaci da kad izbrisemo neki Schedule iz ove liste, on ce postati siroce, i bice automatski obrisan
    // i iz tabele schedule
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="line" /*, orphanRemoval = true*/)
    private List<Schedule> schedules;

    public Line() {
        super();
		this.active = false;
		this.minutesRequiredForWholeRoute = 0;
        this.stops = new ArrayList<>();
        this.points = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    public Line(String name)
    {
        this();
        this.name = name;
    }



    public Line(String name, boolean active, Zone zone, List<Point> points, List<Stop> stops, List<Schedule> schedules)
    {
		this.name = name;
		this.active = active;
		this.zone = zone;
		this.points = points;
		this.stops = stops;
		this.schedules = schedules;
	}
    public Line(String name, int minutesRequiredForWholeRoute, Zone zone, List<Point> points, List<Stop> stops, List<Schedule> schedules) {
        this.name = name;
        this.minutesRequiredForWholeRoute = minutesRequiredForWholeRoute;
        this.zone = zone;
        this.active = true;
        this.points = points;
        this.stops = stops;
        this.schedules = schedules;
    }

	public Line(String name, Zone zone, int minutesRequiredForWholeRoute) {
        this.name = name;
        this.minutesRequiredForWholeRoute = minutesRequiredForWholeRoute;
        this.zone = zone;
        this.active = false;
        this.points = new ArrayList<>();
        this.stops = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinutesRequiredForWholeRoute() {
        return minutesRequiredForWholeRoute;
    }

    public void setMinutesRequiredForWholeRoute(int minutesRequiredForWholeRoute) {
        this.minutesRequiredForWholeRoute = minutesRequiredForWholeRoute;
    }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<Stop> getStops() {
        return stops.stream().filter(s -> !s.isDeleted()).collect(Collectors.toList());
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

   public List<Schedule> getSchedules() {
        return schedules.stream().filter(s -> !s.isDeleted()).collect(Collectors.toList());
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Point> getPoints() {
        return points.stream().filter(s -> !s.isDeleted()).collect(Collectors.toList());
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

	@Override
    public Zone getZone() {
        return zone;
    }

    @Override
    public String getCompleteName() {
        return name.substring(0, name.length()-1) + " (line)";
    }

    @Override
	protected double getPrice(PriceTicket priceTicket) {
		return priceTicket.getPriceLine();
	}

    @Override
    public String toString() {
        return name + " ("+ zone.getName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;
        return Objects.equals(name, line.name) &&
                Objects.equals(zone, line.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, zone);
    }
}
