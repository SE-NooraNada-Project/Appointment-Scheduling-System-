package com.appointment.domain;

public class Appointment {

    private final User user;
    private final TimeSlot slot;
    private final int participantsCount;
    private final String status;

    public Appointment(User user, TimeSlot slot, int participantsCount) {
        this.user = user;
        this.slot = slot;
        this.participantsCount = participantsCount;
        this.status = "CONFIRMED";
        slot.setBooked(true);
    }


    public Appointment(User user, TimeSlot slot) {
        this(user, slot, 1);
    }

    public User getUser() { return user; }
    public TimeSlot getSlot() { return slot; }
    public int getParticipantsCount() { return participantsCount; }
    public String getStatus() { return status; }
}