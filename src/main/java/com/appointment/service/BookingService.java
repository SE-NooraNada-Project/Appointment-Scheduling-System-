package com.appointment.service;

import com.appointment.domain.Appointment;
import com.appointment.domain.AppointmentType;
import com.appointment.domain.Session;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.repository.AppointmentRepository;
import com.appointment.service.rules.AppointmentTypeRule;
import com.appointment.service.rules.BookingRuleStrategy;
import com.appointment.service.time.TimeProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for booking, canceling, and modifying appointments.
 * It applies booking validation rules and optionally stores appointments
 * using a repository.
 * @author Nada, Noora
 * @version 1.0
 */
public class BookingService {

    /** List of booking validation rules applied before creating an appointment. */
    private final List<BookingRuleStrategy> rules = new ArrayList<>();

    /** Rule used to validate participant count against appointment type. */
    private final AppointmentTypeRule appointmentTypeRule = new AppointmentTypeRule();

    /** Repository used to persist appointments, if available. */
    private final AppointmentRepository repository;

    /** Provider used to retrieve the current date and time. */
    private final TimeProvider timeProvider;

    /**
     * Constructs a booking service with validation rules, a repository, and a time provider.
     *
     * @param rules booking validation rules
     * @param repository repository used to store appointments
     * @param timeProvider provider used to retrieve current time
     */
    public BookingService(List<BookingRuleStrategy> rules, AppointmentRepository repository, TimeProvider timeProvider) {
        if (rules != null) {
            this.rules.addAll(rules);
        }
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    /**
     * Constructs a booking service with a repository and a time provider.
     *
     * @param repository repository used to store appointments
     * @param timeProvider provider used to retrieve current time
     */
    public BookingService(AppointmentRepository repository, TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    /**
     * Constructs a booking service with validation rules and a time provider.
     *
     * @param rules booking validation rules
     * @param timeProvider provider used to retrieve current time
     */
    public BookingService(List<BookingRuleStrategy> rules, TimeProvider timeProvider) {
        if (rules != null) {
            this.rules.addAll(rules);
        }
        this.repository = null;
        this.timeProvider = timeProvider;
    }

    /**
     * Constructs a booking service with a repository only.
     * Uses the default system time provider.
     *
     * @param repository repository used to store appointments
     */
    public BookingService(AppointmentRepository repository) {
        this.repository = repository;
        this.timeProvider = new TimeProvider();
    }

    /**
     * Constructs a booking service with validation rules only.
     * Uses the default system time provider.
     *
     * @param rules booking validation rules
     */
    public BookingService(List<BookingRuleStrategy> rules) {
        if (rules != null) {
            this.rules.addAll(rules);
        }
        this.repository = null;
        this.timeProvider = new TimeProvider();
    }

    /**
     * Constructs a booking service without rules or repository.
     * Uses the default system time provider.
     */
    public BookingService() {
        this.repository = null;
        this.timeProvider = new TimeProvider();
    }

    /**
     * Books a default individual appointment for one participant.
     *
     * @param user the user requesting the appointment
     * @param slot the selected time slot
     * @return the created appointment if successful, otherwise null
     */
    public Appointment bookAppointment(User user, TimeSlot slot) {
        if (user == null || slot == null || !slot.isFuture(timeProvider.now())) {
            return null;
        }
        return bookAppointment(user, slot, slot.getDurationMinutes(), 1, AppointmentType.INDIVIDUAL);
    }

    /**
     * Books an individual appointment with a custom duration and participant count.
     *
     * @param user the user requesting the appointment
     * @param slot the selected time slot
     * @param requestedDurationMinutes requested duration in minutes
     * @param participantsCount number of participants
     * @return the created appointment if successful, otherwise null
     */
    public Appointment bookAppointment(User user, TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
        if (slot == null || !slot.isFuture(timeProvider.now())) {
            return null;
        }
        return bookAppointment(user, slot, requestedDurationMinutes, participantsCount, AppointmentType.INDIVIDUAL);
    }

    /**
     * Books an appointment after validating the slot, rules, and appointment type.
     *
     * @param user the user requesting the appointment
     * @param slot the selected time slot
     * @param requestedDurationMinutes requested duration in minutes
     * @param participantsCount number of participants
     * @param type appointment type
     * @return the created appointment if successful, otherwise null
     */
    public Appointment bookAppointment(User user, TimeSlot slot, int requestedDurationMinutes, int participantsCount, AppointmentType type) {

        if (user == null || slot == null || type == null) {
            return null;
        }

        if (!slot.isFuture(timeProvider.now())) {
            return null;
        }

        if (slot.isBooked()) {
            return null;
        }

        for (BookingRuleStrategy rule : rules) {
            if (!rule.isValid(slot, requestedDurationMinutes, participantsCount)) {
                return null;
            }
        }

        if (!appointmentTypeRule.isValid(type, participantsCount)) {
            return null;
        }

        Appointment appointment = new Appointment(user, slot, participantsCount, type);

        slot.setBooked(true);

        if (repository != null) {
            repository.save(appointment);
        }

        return appointment;
    }

    /**
     * Cancels an appointment if it exists, is booked, and its slot is still in the future.
     *
     * @param appointment the appointment to cancel
     * @return true if cancellation succeeds, otherwise false
     */
    public boolean cancelAppointment(Appointment appointment) {

        if (appointment == null) {
            return false;
        }

        TimeSlot slot = appointment.getSlot();

        if (!slot.isFuture(timeProvider.now())) {
            return false;
        }

        if (!slot.isBooked()) {
            return false;
        }

        slot.setBooked(false);

        if (repository != null) {
            repository.delete(appointment);
        }

        return true;
    }

    /**
     * Modifies an existing appointment by moving it to a new slot.
     *
     * @param appointment the appointment to modify
     * @param newSlot the new time slot
     * @return the modified appointment if successful, otherwise null
     */
    public Appointment modifyAppointment(Appointment appointment, TimeSlot newSlot) {

        if (appointment == null || newSlot == null) {
            return null;
        }

        TimeSlot oldSlot = appointment.getSlot();

        if (!oldSlot.isFuture(timeProvider.now())) {
            return null;
        }

        if (!oldSlot.isBooked()) {
            return null;
        }

        if (newSlot.isBooked()) {
            return null;
        }

        oldSlot.setBooked(false);

        Appointment modifiedAppointment = new Appointment(
                appointment.getUser(),
                newSlot,
                appointment.getParticipantsCount(),
                appointment.getType()
        );

        newSlot.setBooked(true);

        if (repository != null) {
            repository.delete(appointment);
            repository.save(modifiedAppointment);
        }

        return modifiedAppointment;
    }

    /**
     * Allows an administrator to cancel an appointment if logged in.
     *
     * @param session current session
     * @param appointment the appointment to cancel
     * @return true if cancellation succeeds, otherwise false
     */
    public boolean adminCancelAppointment(Session session, Appointment appointment) {

        if (session == null || appointment == null) {
            return false;
        }

        if (!session.isAdminLoggedIn()) {
            return false;
        }

        return cancelAppointment(appointment);
    }

    /**
     * Allows an administrator to modify an appointment if logged in.
     *
     * @param session current session
     * @param appointment the appointment to modify
     * @param newSlot the new time slot
     * @return the modified appointment if successful, otherwise null
     */
    public Appointment adminModifyAppointment(Session session, Appointment appointment, TimeSlot newSlot) {

        if (session == null || !session.isAdminLoggedIn()) {
            return null;
        }

        return modifyAppointment(appointment, newSlot);
    }
}