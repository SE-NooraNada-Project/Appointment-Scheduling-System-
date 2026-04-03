package com.appointment.service.notification;

import com.appointment.domain.Appointment;
import com.appointment.domain.User;

import java.util.List;

public class ReminderService {

    private final Observer notificationService;

    public ReminderService(Observer notificationService) {
        this.notificationService = notificationService;
    }

    public void sendReminders(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            User user = appointment.getUser();

            String message = "Reminder: You have an appointment at "
                    + appointment.getSlot().getStart();

            notificationService.notify(user, message);
        }
    }
}