package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.TicketType;

public class TicketDTO {
    private boolean hasZoneNotLine;
    private Long id;
    private int dayInMonthOrMonthInYear;
    private TicketType ticketType;

    public TicketDTO() {

    }

    public TicketDTO(boolean hasZoneNotLine, Long id, int dayInMonthOrMonthInYear, TicketType ticketType) {
        this.hasZoneNotLine = hasZoneNotLine;
        this.id = id;
        this.dayInMonthOrMonthInYear = dayInMonthOrMonthInYear;
        this.ticketType = ticketType;
    }

    public boolean hasZoneNotLine() {
        return hasZoneNotLine;
    }

    public void setHasZoneNotLine(boolean hasZoneNotLine) {
        this.hasZoneNotLine = hasZoneNotLine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
