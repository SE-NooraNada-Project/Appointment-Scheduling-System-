package com.appointment.service.rules;

import com.appointment.domain.TimeSlot;

public class ParticipantLimitRule implements BookingRuleStrategy {

    @Override
    public boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
        return participantsCount <= slot.getMaxParticipants();
    }

    @Override
    public String errorMessage() {
        return "Invalid participants: exceeds slot capacity";
    }
}