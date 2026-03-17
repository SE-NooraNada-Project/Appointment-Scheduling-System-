package com.appointment.service.rules;

import com.appointment.domain.TimeSlot;

public interface BookingRuleStrategy {
    boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount);
    String errorMessage();
}