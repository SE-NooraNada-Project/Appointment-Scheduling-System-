package com.appointment.service.rules;

import com.appointment.domain.AppointmentType;

/**
 * Validation rule that ensures the number of participants
 * is appropriate for the selected appointment type.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public class AppointmentTypeRule {

    /**
     * Validates the participant count based on the appointment type.
     *
     * @param type appointment type
     * @param participantsCount number of participants
     * @return true if the participant count is valid for the given type, false otherwise
     */
    public boolean isValid(AppointmentType type, int participantsCount) {
        if (type == AppointmentType.INDIVIDUAL) {
            return participantsCount == 1;
        }

        if (type == AppointmentType.GROUP) {
            return participantsCount > 1;
        }

        if (type == AppointmentType.ASSESSMENT || type == AppointmentType.FOLLOW_UP) {
            return participantsCount == 1;
        }

        return participantsCount > 0;
    }

    /**
     * Returns the error message when validation fails.
     *
     * @return error message describing the rule violation
     */
    public String errorMessage() {
        return "Invalid participants count for appointment type";
    }
}