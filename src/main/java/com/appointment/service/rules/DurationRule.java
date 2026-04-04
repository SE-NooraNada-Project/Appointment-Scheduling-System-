package com.appointment.service.rules;

import com.appointment.domain.TimeSlot;

/**
 * Validation rule that checks whether the requested appointment duration
 * does not exceed the maximum allowed duration.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public class DurationRule implements BookingRuleStrategy {

    /** Maximum allowed duration in minutes for an appointment. */
    private final int maxAllowedMinutes;

    /**
     * Constructs a duration rule with a maximum allowed duration.
     *
     * @param maxAllowedMinutes maximum allowed duration in minutes
     */
    public DurationRule(int maxAllowedMinutes) {
        this.maxAllowedMinutes = maxAllowedMinutes;
    }

    /**
     * Validates that the requested duration does not exceed the maximum allowed duration.
     *
     * @param slot the selected time slot
     * @param requestedDurationMinutes requested duration in minutes
     * @param participantsCount number of participants
     * @return true if the requested duration is valid, false otherwise
     */
    @Override
    public boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
        return requestedDurationMinutes <= maxAllowedMinutes;
    }

    /**
     * Returns the error message for invalid appointment duration.
     *
     * @return error message describing the duration rule violation
     */
    @Override
    public String errorMessage() {
        return "Invalid duration: exceeds maximum allowed minutes";
    }
}