package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Line;

public class LineDTO {
    private Long id;
    private String name;

    public LineDTO() {

    }

    public LineDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public LineDTO(Line line) {
        this.id = line.getId();
        this.name = line.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
