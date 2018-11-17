package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Zone extends LineZone {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;*/

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Line> lines;

    public Zone() {

    }

    public Zone(String name, List<Line> lines) {
        this.name = name;
        this.lines = lines;
    }

    public Zone(String name) {
        this.name = name;
        this.lines = new ArrayList<Line>();

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
    protected Zone getZone() {
        return this;
    }
}
