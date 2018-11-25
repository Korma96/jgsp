package com.mjvs.jgsp.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Zone extends LineZone {
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Line> lines;

    public Zone(String name, @NotNull List<Line> lines) {
        this.name = name;
        this.lines = lines;
    }

    public Zone(String name) {
        this.name = name;
        this.lines = new ArrayList<>();
    }

    public Zone()
    {
        this.lines = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
	protected double getPrice(PriceTicket priceTicket) {
		return priceTicket.getPriceZone();
	}
}
