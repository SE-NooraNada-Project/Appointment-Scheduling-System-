package com.appointment.service;

import com.appointment.domain.Appointment;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.rules.BookingRuleStrategy;

import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private final List<BookingRuleStrategy> rules = new ArrayList<>();

    public BookingService(List<BookingRuleStrategy> rules) {
        if (rules != null) this.rules.addAll(rules);
    }

    public BookingService() {}

    public Appointment bookAppointment(User user, TimeSlot slot) {
        return bookAppointment(user, slot, slot.getDurationMinutes(), 1);
    }

    public Appointment bookAppointment(User user, TimeSlot slot, int requestedDurationMinutes, int participantsCount) {

        if (slot.isBooked()) return null;

        for (BookingRuleStrategy rule : rules) {
            if (!rule.isValid(slot, requestedDurationMinutes, participantsCount)) {
                return null;
            }
        }

        return new Appointment(user, slot, participantsCount);
    }
}