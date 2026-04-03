package com.appointment.service.notification;

import com.appointment.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockNotificationService implements Observer {

    private final List<String> sentMessages = new ArrayList<>();

    @Override
    public void notify(User user, String message) {
        sentMessages.add(user.getName() + ": " + message);
    }

    public List<String> getSentMessages() {
        return sentMessages;
    }
}