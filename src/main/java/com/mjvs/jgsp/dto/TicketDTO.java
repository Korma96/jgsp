package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.TicketType;

public class TicketDTO {
    private boolean hasZoneNotLine;
    private String name;
    private int dayInMonthOrMonthInYear;
    private TicketType ticketType;

    public TicketDTO() {

    }

    public TicketDTO(boolean hasZoneNotLine, String id, int dayInMonthOrMonthInYear, TicketType ticketType) {
        this.hasZoneNotLine = hasZoneNotLine;
        this.name = name;
        this.dayInMonthOrMonthInYear = dayInMonthOrMonthInYear;
        this.ticketType = ticketType;
    }

    public boolean hasZoneNotLine() {
        return hasZoneNotLine;
    }

    public void setHasZoneNotLine(boolean hasZoneNotLine) {
        this.hasZoneNotLine = hasZoneNotLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayInMonthOrMonthInYear() {
        return dayInMonthOrMonthInYear;
    }

    public void setDayInMonthOrMonthInYear(int dayInMonthOrMonthInYear) {
        this.dayInMonthOrMonthInYear = dayInMonthOrMonthInYear;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
}
