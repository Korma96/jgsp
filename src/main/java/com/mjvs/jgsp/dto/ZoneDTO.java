package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.TransportType;

import java.util.List;

public class ZoneDTO {
    private String name;
    private String transport;
    private List<LineDTO> lines;

    public ZoneDTO() {

    }

    public ZoneDTO(String name, List<LineDTO> lines, TransportType transport) {
        this.name = name;
        this.transport = transport.name().toLowerCase();
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public List<LineDTO> getLines() {
        return lines;
    }

    public void setLines(List<LineDTO> lines) {
        this.lines = lines;
    }
}
