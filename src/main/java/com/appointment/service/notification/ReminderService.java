package com.appointment.service.notification;

import com.appointment.domain.Appointment;
import com.appointment.domain.User;

import java.util.List;

/**
 * Service responsible for sending appointment reminder notifications.
 */
public class ReminderService {

    /** Notification service used to send reminders */
    private final Observer notificationService;

    /**
     * Constructs a ReminderService with a notification observer.
     *
     * @param notificationService observer used for notifications
     */
    public ReminderService(Observer notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Sends reminders for a list of appointments.
     *
     * @param appointments list of appointments
     */
    public void sendReminders(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            User user = appointment.getUser();

            String message = "Reminder: You have an appointment at "
                    + appointment.getSlot().getStart();

            notificationService.notify(user, message);
        }
    }
}