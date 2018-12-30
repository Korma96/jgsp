package com.mjvs.jgsp.dto;

import java.util.List;

public class TimesDTO {
    private List<String> timesA;
    private List<String> timesB;

    public TimesDTO() {

    }

    public TimesDTO(List<String> timesA, List<String> timesB) {
        this.timesA = timesA;
        this.timesB = timesB;
    }

    public List<String> getTimesA() {
        return timesA;
    }

    public void setTimesA(List<String> timesA) {
        this.timesA = timesA;
    }

    public List<String> getTimesB() {
        return timesB;
    }

    public void setTimesB(List<String> timesB) {
        this.timesB = timesB;
    }

}
