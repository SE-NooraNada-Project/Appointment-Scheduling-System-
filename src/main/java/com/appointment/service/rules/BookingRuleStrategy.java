package com.appointment.service.rules;

import com.appointment.domain.TimeSlot;

/**
 * Strategy interface for booking validation rules.
 * Defines methods that must be implemented to validate
 * appointment constraints such as duration, participants, or type.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public interface BookingRuleStrategy {

    /**
     * Validates whether the given booking parameters satisfy the rule.
     *
     * @param slot the selected time slot
     * @param requestedDurationMinutes requested duration in minutes
     * @param participantsCount number of participants
     * @return true if the rule is satisfied, false otherwise
     */
    boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount);

    /**
     * Returns the error message associated with this rule.
     *
     * @return error message describing the validation failure
     */
    String errorMessage();
}