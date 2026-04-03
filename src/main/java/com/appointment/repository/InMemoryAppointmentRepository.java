package com.appointment.repository;

import com.appointment.domain.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the AppointmentRepository interface.
 * Stores appointments in a local list during runtime.
 */
public class InMemoryAppointmentRepository implements AppointmentRepository {

    /** Internal list used to store appointments */
    private final List<Appointment> appointments = new ArrayList<>();

    /**
     * Saves a non-null appointment to the in-memory list.
     *
     * @param appointment the appointment to save
     */
    @Override
    public void save(Appointment appointment) {
        if (appointment != null) {
            appointments.add(appointment);
        }
    }

    /**
     * Retrieves all stored appointments.
     *
     * @return list of all appointments
     */
    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(appointments);
    }

    /**
     * Finds appointments associated with a specific user ID.
     *
     * @param userId the user ID to search for
     * @return list of matching appointments
     */
    @Override
    public List<Appointment> findByUserId(String userId) {
        List<Appointment> result = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getUser().getId().equals(userId)) {
                result.add(appointment);
            }
        }

        return result;
    }

    /**
     * Deletes an appointment from storage.
     *
     * @param appointment the appointment to delete
     * @return true if removed successfully, false otherwise
     */
    @Override
    public boolean delete(Appointment appointment) {
        return appointments.remove(appointment);
    }
}