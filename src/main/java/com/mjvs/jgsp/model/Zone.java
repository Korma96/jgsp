package com.mjvs.jgsp.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Zone extends LineZone {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "transport_type", unique = false, nullable = false)
    private TransportType transportType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Line> lines;

    public Zone(String name, @NotNull List<Line> lines, TransportType transportType) {
        super();
        this.name = name;
        this.lines = lines;
        this.transportType = transportType;
    }

    public Zone(String name, TransportType transportType) {
        this.name = name;
        this.transportType = transportType;
        this.lines = new ArrayList<>();
    }

    public void addLine(Line line) { lines.add(line); }

    public Zone()
    {
        this.lines = new ArrayList<>();
        this.transportType = TransportType.BUS;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public Zone getZone() {
        return this;
    }

    @Override
    public String getCompleteName() {
        return name + " (zone)";
    }

    @Override
	protected double getPrice(PriceTicket priceTicket) {
		return priceTicket.getPriceZone();
	}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Zone zone = (Zone) o;
        return Objects.equals(name, zone.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
