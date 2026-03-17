package com.appointment.service.rules;

import com.appointment.domain.TimeSlot;

public class DurationRule implements BookingRuleStrategy {

    private final int maxAllowedMinutes;

    public DurationRule(int maxAllowedMinutes) {
        this.maxAllowedMinutes = maxAllowedMinutes;
    }

    @Override
    public boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
        return requestedDurationMinutes <= maxAllowedMinutes;
    }

    @Override
    public String errorMessage() {
        return "Invalid duration: exceeds maximum allowed minutes";
    }
}