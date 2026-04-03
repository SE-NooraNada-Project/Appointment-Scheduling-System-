package com.appointment.repository;

import com.appointment.domain.Appointment;

import java.util.List;

/**
 * Repository interface for managing appointment persistence.
 * Defines basic operations for saving, retrieving, and deleting appointments.
 */
public interface AppointmentRepository {

    /**
     * Saves an appointment.
     *
     * @param appointment the appointment to save
     */
    void save(Appointment appointment);

    /**
     * Retrieves all appointments.
     *
     * @return list of all appointments
     */
    List<Appointment> findAll();

    /**
     * Finds appointments by user ID.
     *
     * @param userId the user ID
     * @return list of matching appointments
     */
    List<Appointment> findByUserId(String userId);

    /**
     * Deletes an appointment.
     *
     * @param appointment the appointment to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean delete(Appointment appointment);
}