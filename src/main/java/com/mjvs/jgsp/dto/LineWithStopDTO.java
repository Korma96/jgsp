package com.mjvs.jgsp.dto;

public class LineWithStopDTO
{
    private Long lineId;

    private Long stopId;

    public LineWithStopDTO(Long lineId, Long stopId) {
        this.lineId = lineId;
        this.stopId = stopId;
    }

    public LineWithStopDTO()
    {

    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }
}
