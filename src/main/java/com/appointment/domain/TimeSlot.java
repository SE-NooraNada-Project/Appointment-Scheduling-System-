package com.appointment.domain;

import java.time.LocalDateTime;

/**
 * Represents a time slot available for booking appointments.
 * A time slot has a start time, duration, maximum participant capacity,
 * and a booking status.
 * @author Nada, Noora
 * @version 1.0
 */
public class TimeSlot {

    /** Start date and time of the slot */
    private final LocalDateTime start;

    /** Duration of the slot in minutes */
    private final int durationMinutes;

    /** Maximum number of participants allowed in the slot */
    private final int maxParticipants;

    /** Indicates whether the slot is currently booked */
    private boolean booked;

    /**
     * Constructs a time slot with full details.
     *
     * @param start start date and time
     * @param durationMinutes duration in minutes
     * @param maxParticipants maximum allowed participants
     */
    public TimeSlot(LocalDateTime start, int durationMinutes, int maxParticipants) {
        this.start = start;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.booked = false;
    }

    /**
     * Constructs a time slot with default one participant.
     *
     * @param start start date and time
     * @param durationMinutes duration in minutes
     */
    public TimeSlot(LocalDateTime start, int durationMinutes) {
        this(start, durationMinutes, 1);
    }

    /**
     * Gets the slot start date and time.
     *
     * @return start date and time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets the slot duration in minutes.
     *
     * @return duration in minutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Gets the maximum number of participants.
     *
     * @return maximum participants
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Checks whether the slot start time is after the provided current time.
     *
     * @param currentTime current date and time
     * @return true if the slot is in the future, false otherwise
     */
    public boolean isFuture(LocalDateTime currentTime) {
        return start.isAfter(currentTime);
    }

    /**
     * Checks whether the slot is booked.
     *
     * @return true if booked, false otherwise
     */
    public boolean isBooked() {
        return booked;
    }

    /**
     * Sets the booking status of the slot.
     *
     * @param booked booking status
     */
    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}