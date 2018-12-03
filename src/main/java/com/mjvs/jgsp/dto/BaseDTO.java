package com.mjvs.jgsp.dto;

public class BaseDTO
{
    private Long id;
    private String name;

    public BaseDTO() {

    }

    public BaseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
