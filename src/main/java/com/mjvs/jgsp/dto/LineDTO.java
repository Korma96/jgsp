package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Line;

public class LineDTO
{
    private String name;

    public LineDTO() {
    }

    public LineDTO(Line line)
    {
        this.name = line.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
