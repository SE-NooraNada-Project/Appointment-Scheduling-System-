package com.appointment.presentation;

import com.appointment.domain.*;
import com.appointment.service.AuthService;

import java.time.LocalDateTime;

/**
 * Demonstrates the basic workflow of the appointment scheduling system.
 * This includes administrator login, displaying available slots,
 * and administrator logout.
 */
public class Main {

    /**
     * Runs a simple demo of the appointment scheduling system.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Administrator admin = new Administrator("1","Admin","admin","1234");

        Session session = new Session();
        AuthService auth = new AuthService();

        boolean success = auth.login(session, admin, "admin", "1234");

        System.out.println("Login success: " + success);

        Schedule schedule = new Schedule();

        schedule.addSlot(new TimeSlot(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),30));
        schedule.addSlot(new TimeSlot(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0),30));

        System.out.println("Available Slots:");

        for(TimeSlot s : schedule.getAvailableSlots()){
            System.out.println(s.getStart());
        }

        auth.logout(session);

        System.out.println("Admin logged in? " + session.isAdminLoggedIn());

    }
}