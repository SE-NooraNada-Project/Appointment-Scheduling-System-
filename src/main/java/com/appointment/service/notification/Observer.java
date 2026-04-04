package com.appointment.service.notification;

import com.appointment.domain.User;

/**
 * Observer interface used for notification services.
 * Defines the method required to send notifications to users.
 *
 * @author Nada, Noora
 * @version 1.0
 */
public interface Observer {

    /**
     * Sends a notification message to a user.
     *
     * @param user the target user
     * @param message the notification message
     */
    void notify(User user, String message);
}