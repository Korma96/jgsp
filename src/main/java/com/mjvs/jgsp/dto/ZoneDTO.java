package com.mjvs.jgsp.dto;

import java.util.List;

public class ZoneDTO
{
    private String name;

    private List<String> lineNames;

    public ZoneDTO() {
    }

    public ZoneDTO(String name) {
        this.name = name;
    }

    public List<String> getLineNames() {
        return lineNames;
    }

    public void setLineNames(List<String> lineNames) {
        this.lineNames = lineNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
