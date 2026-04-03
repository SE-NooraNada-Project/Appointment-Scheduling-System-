package com.appointment.service.notification;

import com.appointment.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock implementation of the Observer interface.
 * Used in tests to store sent notification messages.
 */
public class MockNotificationService implements Observer {

    /** List of sent messages recorded during testing */
    private final List<String> sentMessages = new ArrayList<>();

    /**
     * Records a notification message for a user.
     *
     * @param user the user receiving the notification
     * @param message the notification message
     */
    @Override
    public void notify(User user, String message) {
        sentMessages.add(user.getName() + ": " + message);
    }

    /**
     * Gets all recorded sent messages.
     *
     * @return list of sent messages
     */
    public List<String> getSentMessages() {
        return sentMessages;
    }
}