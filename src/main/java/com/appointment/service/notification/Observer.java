package com.appointment.service.notification;

import com.appointment.domain.User;

public interface Observer {
    void notify(User user, String message);
}