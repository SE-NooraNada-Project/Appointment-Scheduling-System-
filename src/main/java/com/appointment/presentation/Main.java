package com.appointment.presentation;

import com.appointment.domain.*;
import com.appointment.service.AuthService;
import com.appointment.service.notification.EmailNotificationService;
import com.appointment.service.notification.ReminderService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Demonstrates the basic workflow of the appointment scheduling system.
 * This includes administrator login, displaying available slots,
 * sending a reminder email, and administrator logout.
 */
public class Main {

    public static void main(String[] args) {

        Administrator admin = new Administrator("1", "Admin", "admin", "1234");

        Session session = new Session();
        AuthService auth = new AuthService();

        boolean success = auth.login(session, admin, "admin", "1234");
        System.out.println("Login success: " + success);

        Schedule schedule = new Schedule();

        TimeSlot slot1 = new TimeSlot(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), 30);
        TimeSlot slot2 = new TimeSlot(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0), 30);

        schedule.addSlot(slot1);
        schedule.addSlot(slot2);

        System.out.println("Available Slots:");
        for (TimeSlot s : schedule.getAvailableSlots()) {
            System.out.println(s.getStart());
        }

        // send reminder email
        User user = new User("2", "Test User");
        Appointment appointment = new Appointment(user, slot1);

        ReminderService reminderService = new ReminderService(new EmailNotificationService());
        reminderService.sendReminders(List.of(appointment));

        auth.logout(session);
        System.out.println("Admin logged in? " + session.isAdminLoggedIn());
    }
}