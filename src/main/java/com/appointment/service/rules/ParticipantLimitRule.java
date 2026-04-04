package com.appointment.service.rules;

import com.appointment.domain.TimeSlot;

/**
 * Validation rule that checks whether the number of participants
 * does not exceed the capacity of the selected time slot.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public class ParticipantLimitRule implements BookingRuleStrategy {

    /**
     * Validates that the number of participants does not exceed the slot capacity.
     *
     * @param slot the selected time slot
     * @param requestedDurationMinutes requested duration in minutes
     * @param participantsCount number of participants
     * @return true if the participant count is valid, false otherwise
     */
    @Override
    public boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
        return participantsCount <= slot.getMaxParticipants();
    }

    /**
     * Returns the error message for invalid participant count.
     *
     * @return error message describing the participant limit violation
     */
    @Override
    public String errorMessage() {
        return "Invalid participants: exceeds slot capacity";
    }
}