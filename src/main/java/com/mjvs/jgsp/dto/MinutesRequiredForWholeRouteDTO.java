package com.mjvs.jgsp.dto;

public class MinutesRequiredForWholeRouteDTO
{
    private int minutes;

    public MinutesRequiredForWholeRouteDTO(int minutes) {
        this.minutes = minutes;
    }

    public MinutesRequiredForWholeRouteDTO() {}

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
