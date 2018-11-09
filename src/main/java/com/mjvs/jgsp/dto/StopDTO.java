package com.mjvs.jgsp.dto;

public class StopDTO {

    private String name;

    public StopDTO()
    {

    }

    public StopDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
