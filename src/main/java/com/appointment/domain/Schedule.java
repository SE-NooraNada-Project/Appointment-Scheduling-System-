package com.appointment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a schedule containing multiple time slots.
 * Provides methods for adding and retrieving available slots.
 */
public class Schedule {

    /** List of all time slots in the schedule */
    private final List<TimeSlot> slots = new ArrayList<>();

    /**
     * Adds a time slot to the schedule.
     *
     * @param slot the time slot to add
     */
    public void addSlot(TimeSlot slot) {
        slots.add(slot);
    }

    /**
     * Gets all time slots in the schedule.
     *
     * @return list of all time slots
     */
    public List<TimeSlot> getAllSlots() {
        return new ArrayList<>(slots);
    }

    /**
     * Gets only the available unbooked time slots.
     *
     * @return list of available time slots
     */
    public List<TimeSlot> getAvailableSlots() {
        return slots.stream()
                .filter(s -> !s.isBooked())
                .collect(Collectors.toList());
    }
}