package com.mjvs.jgsp.dto;

public class ZoneWithLineDTO
{
    private Long zoneId;

    private Long lineId;

    public ZoneWithLineDTO() {
    }

    public ZoneWithLineDTO(Long zoneId, Long lineId) {
        this.zoneId = zoneId;
        this.lineId = lineId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }
}
