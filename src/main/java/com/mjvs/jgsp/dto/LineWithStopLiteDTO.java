package com.mjvs.jgsp.dto;

public class LineWithStopLiteDTO
{
    private Long lineId;

    private Long stopId;

    // stop position in line stops
    private int position;

    public LineWithStopLiteDTO(Long lineId, Long stopId) {
        this.lineId = lineId;
        this.stopId = stopId;
    }

    public LineWithStopLiteDTO()
    {

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
