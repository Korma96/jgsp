package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Line;

public class LineDtoFromFrontend
{
    private String lineName;
    private Long zoneId;

    public LineDtoFromFrontend() {
    }

    public LineDtoFromFrontend(String lineName, Long zoneId) {
        this.lineName = lineName;
        this.zoneId = zoneId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }
}
