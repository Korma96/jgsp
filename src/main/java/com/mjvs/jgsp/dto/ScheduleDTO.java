package com.mjvs.jgsp.dto;

import java.util.List;

public class ScheduleDTO
{
    long lineId;
    String dateFrom;
    List<List<String>> times;

    public ScheduleDTO(){

    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public List<List<String>> getTimes() {
        return times;
    }

    public void setTimes(List<List<String>> times) {
        this.times = times;
    }
}
