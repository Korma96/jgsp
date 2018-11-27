package com.mjvs.jgsp.dto;

public class LineWithScheduleDTO
{
    private Long lineId;

    private Long scheduleId;

    public LineWithScheduleDTO(Long lineId, Long scheduleId) {
        this.lineId = lineId;
        this.scheduleId = scheduleId;
    }

    public LineWithScheduleDTO()
    {

    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
