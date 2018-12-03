package com.mjvs.jgsp.dto;

public class BaseLiteDTO
{
    private String name;

    public BaseLiteDTO()
    {

    }

    public BaseLiteDTO(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
