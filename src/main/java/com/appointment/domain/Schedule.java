package com.appointment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {
    private final List<TimeSlot> slots = new ArrayList<>();

    public void addSlot(TimeSlot slot) {
        slots.add(slot);
    }

    public List<TimeSlot> getAllSlots() {
        return new ArrayList<>(slots);
    }

    public List<TimeSlot> getAvailableSlots() {
        return slots.stream()
                .filter(s -> !s.isBooked())
                .collect(Collectors.toList());
    }
}