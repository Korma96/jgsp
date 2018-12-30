package com.mjvs.jgsp.dto;

import java.util.List;

public class ZoneDTO {
    private String name;
    private List<BaseDTO> lines;

    public ZoneDTO(String name, List<BaseDTO> lines) {
        this.name = name;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseDTO> getLines() {
        return lines;
    }

    public void setLines(List<BaseDTO> lines) {
        this.lines = lines;
    }
}
