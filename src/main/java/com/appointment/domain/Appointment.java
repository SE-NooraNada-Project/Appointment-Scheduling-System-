package com.appointment.domain;

/**
 * Represents an appointment booked by a user.
 * Contains details such as the associated time slot,
 * number of participants, appointment type, and status.
 */
public class Appointment {

    /** The user who booked the appointment */
    private final User user;

    /** The time slot assigned to the appointment */
    private final TimeSlot slot;

    /** Number of participants in the appointment */
    private final int participantsCount;

    /** Status of the appointment (e.g., CONFIRMED) */
    private final String status;

    /** Type of the appointment (INDIVIDUAL, GROUP, etc.) */
    private final AppointmentType type;

    /**
     * Constructs a new Appointment with full details.
     *
     * @param user the user booking the appointment
     * @param slot the selected time slot
     * @param participantsCount number of participants
     * @param type appointment type
     */
    public Appointment(User user, TimeSlot slot, int participantsCount, AppointmentType type) {
        this.user = user;
        this.slot = slot;
        this.participantsCount = participantsCount;
        this.type = type;
        this.status = "CONFIRMED";
        slot.setBooked(true);
    }

    /**
     * Constructs a default individual appointment with one participant.
     *
     * @param user the user booking the appointment
     * @param slot the selected time slot
     */
    public Appointment(User user, TimeSlot slot) {
        this(user, slot, 1, AppointmentType.INDIVIDUAL);
    }

    /**
     * Gets the user.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the time slot.
     *
     * @return time slot
     */
    public TimeSlot getSlot() {
        return slot;
    }

    /**
     * Gets the number of participants.
     *
     * @return participants count
     */
    public int getParticipantsCount() {
        return participantsCount;
    }

    /**
     * Gets the appointment status.
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the appointment type.
     *
     * @return appointment type
     */
    public AppointmentType getType() {
        return type;
    }
}