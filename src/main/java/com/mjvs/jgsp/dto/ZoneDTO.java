package com.mjvs.jgsp.dto;

public class ZoneDTO
{
    private String name;

    private String lineName;

    public ZoneDTO() {
    }

    public ZoneDTO(String name) {
        this.name = name;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
