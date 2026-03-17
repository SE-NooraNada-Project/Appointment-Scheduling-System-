package com.appointment.domain;

import java.time.LocalDateTime;

public class TimeSlot {
    private final LocalDateTime start;
    private final int durationMinutes;
    private final int maxParticipants;
    private boolean booked;

    public TimeSlot(LocalDateTime start, int durationMinutes, int maxParticipants) {
        this.start = start;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.booked = false;
    }


    public TimeSlot(LocalDateTime start, int durationMinutes) {
        this(start, durationMinutes, 1);
    }

    public LocalDateTime getStart() { return start; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getMaxParticipants() { return maxParticipants; }

    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
}