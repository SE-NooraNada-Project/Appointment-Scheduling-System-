package com.appointment.service;

import com.appointment.domain.Appointment;
import com.appointment.domain.AppointmentType;
import com.appointment.domain.Session;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.rules.AppointmentTypeRule;
import com.appointment.service.rules.BookingRuleStrategy;

import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private final List<BookingRuleStrategy> rules = new ArrayList<>();
    private final AppointmentTypeRule appointmentTypeRule = new AppointmentTypeRule();

    public BookingService(List<BookingRuleStrategy> rules) {
        if (rules != null) this.rules.addAll(rules);
    }

    public BookingService() {}

    public Appointment bookAppointment(User user, TimeSlot slot) {
        return bookAppointment(user, slot, slot.getDurationMinutes(), 1, AppointmentType.INDIVIDUAL);
    }

    public Appointment bookAppointment(User user, TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
        return bookAppointment(user, slot, requestedDurationMinutes, participantsCount, AppointmentType.INDIVIDUAL);
    }

    public Appointment bookAppointment(User user, TimeSlot slot, int requestedDurationMinutes, int participantsCount, AppointmentType type) {

        if (slot.isBooked()) return null;

        for (BookingRuleStrategy rule : rules) {
            if (!rule.isValid(slot, requestedDurationMinutes, participantsCount)) {
                return null;
            }
        }

        if (!appointmentTypeRule.isValid(type, participantsCount)) {
            return null;
        }

        return new Appointment(user, slot, participantsCount, type);
    }

    public boolean cancelAppointment(Appointment appointment) {

        if (appointment == null) return false;

        TimeSlot slot = appointment.getSlot();

        if (!slot.isBooked()) return false;

        slot.setBooked(false);

        return true;
    }

    public boolean modifyAppointment(Appointment appointment, TimeSlot newSlot) {

        if (appointment == null || newSlot == null) return false;

        TimeSlot oldSlot = appointment.getSlot();

        if (!oldSlot.isBooked()) return false;

        if (newSlot.isBooked()) return false;

        oldSlot.setBooked(false);
        newSlot.setBooked(true);

        return true;
    }

    public boolean adminCancelAppointment(Session session, Appointment appointment) {

        if (session == null || appointment == null) return false;

        if (!session.isAdminLoggedIn()) return false;

        return cancelAppointment(appointment);
    }
}