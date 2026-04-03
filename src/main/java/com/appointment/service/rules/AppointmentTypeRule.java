package com.appointment.service.rules;

import com.appointment.domain.AppointmentType;
import com.appointment.domain.TimeSlot;

public class AppointmentTypeRule {

    public boolean isValid(AppointmentType type, int participantsCount) {
        if (type == AppointmentType.INDIVIDUAL) {
            return participantsCount == 1;
        }

        if (type == AppointmentType.GROUP) {
            return participantsCount > 1;
        }

        return true;
    }

    public String errorMessage() {
        return "Invalid participants count for appointment type";
    }
}