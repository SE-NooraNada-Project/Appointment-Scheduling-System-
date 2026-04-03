package com.appointment.domain;

public class Appointment {

    private final User user;
    private final TimeSlot slot;
    private final int participantsCount;
    private final String status;
    private final AppointmentType type;

    public Appointment(User user, TimeSlot slot, int participantsCount, AppointmentType type) {
        this.user = user;
        this.slot = slot;
        this.participantsCount = participantsCount;
        this.type = type;
        this.status = "CONFIRMED";
        slot.setBooked(true);
    }

    public Appointment(User user, TimeSlot slot) {
        this(user, slot, 1, AppointmentType.INDIVIDUAL);
    }

    public User getUser() { return user; }
    public TimeSlot getSlot() { return slot; }
    public int getParticipantsCount() { return participantsCount; }
    public String getStatus() { return status; }
    public AppointmentType getType() { return type; }
}