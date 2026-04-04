package com.appointment.service.time;

import java.time.LocalDateTime;

/**
 * Provides the current date and time.
 * This abstraction allows time-related logic to be tested using mocks.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public class TimeProvider {

    /**
     * Returns the current system date and time.
     *
     * @return current date and time
     */
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}