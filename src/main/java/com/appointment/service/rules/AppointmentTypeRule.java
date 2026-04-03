package com.appointment.service.rules;

import com.appointment.domain.AppointmentType;

/**
 * Validates participant count against the selected appointment type.
 */
public class AppointmentTypeRule {

    /**
     * Checks whether the participants count is valid for the appointment type.
     *
     * @param type appointment type
     * @param participantsCount number of participants
     * @return true if valid, false otherwise
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
     * Returns the error message for invalid type rules.
     *
     * @return error message
     */
    public String errorMessage() {
        return "Invalid participants count for appointment type";
    }
}