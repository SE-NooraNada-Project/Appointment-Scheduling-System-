package com.appointment.presentation;

import com.appointment.domain.Administrator;
import com.appointment.domain.Appointment;
import com.appointment.domain.AppointmentType;
import com.appointment.domain.Session;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.repository.InMemoryAppointmentRepository;
import com.appointment.service.AuthService;
import com.appointment.service.BookingService;
import com.appointment.service.notification.EmailNotificationService;
import com.appointment.service.notification.MockNotificationService;
import com.appointment.service.notification.ReminderService;
import com.appointment.service.rules.BookingRuleStrategy;
import com.appointment.service.rules.DurationRule;
import com.appointment.service.rules.ParticipantLimitRule;
import com.appointment.service.time.TimeProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface for the Appointment Scheduling System.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Administrator admin =
            new Administrator("A1", "System Admin", "admin@email.com", "admin", "1234");

    private static final Session session = new Session();
    private static final AuthService authService = new AuthService();
    private static final InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
    private static final BookingService bookingService;

    private static final MockNotificationService mockNotificationService;
    private static final ReminderService mockReminderService;
    private static final ReminderService emailReminderService;

    private static final List<TimeSlot> slots = new ArrayList<>();

    static {
        List<BookingRuleStrategy> rules = new ArrayList<>();
        rules.add(new DurationRule(120));
        rules.add(new ParticipantLimitRule());

        bookingService = new BookingService(rules, repository, new TimeProvider());

        mockNotificationService = new MockNotificationService();
        mockReminderService = new ReminderService(mockNotificationService);
        emailReminderService = new ReminderService(new EmailNotificationService());

        seedSlots();
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      Appointment Scheduling System");
        System.out.println("========================================");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> administratorLogin();
                case 2 -> administratorLogout();
                case 3 -> viewAvailableSlots();
                case 4 -> bookAppointment();
                case 5 -> viewMyAppointments();
                case 6 -> modifyMyAppointment();
                case 7 -> cancelMyAppointment();
                case 8 -> administratorManageReservations();
                case 9 -> sendMockAppointmentReminders();
                case 10 -> sendEmailAppointmentReminders();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting system. Goodbye.");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }
    }

    private static void printMainMenu() {
        System.out.println("--------------- Main Menu ---------------");
        System.out.println("1. Administrator Login");
        System.out.println("2. Administrator Logout");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Book Appointment");
        System.out.println("5. View My Appointments");
        System.out.println("6. Modify My Appointment");
        System.out.println("7. Cancel My Appointment");
        System.out.println("8. Administrator Manage Reservations");
        System.out.println("9. Send Mock Appointment Reminders");
        System.out.println("10. Send Email Appointment Reminders");
        System.out.println("0. Exit");
        System.out.println("-----------------------------------------");
    }

    private static void administratorLogin() {
        if (session.isAdminLoggedIn()) {
            System.out.println("Administrator is already logged in.");
            return;
        }

        String username = readLine("Enter admin username: ");
        String password = readLine("Enter admin password: ");

        boolean success = authService.login(session, admin, username, password);

        if (success) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void administratorLogout() {
        if (!session.isAdminLoggedIn()) {
            System.out.println("No administrator is currently logged in.");
            return;
        }

        authService.logout(session);
        System.out.println("Administrator logged out successfully.");
    }

    private static void viewAvailableSlots() {
        List<TimeSlot> availableSlots = getAvailableSlots();

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots found.");
            return;
        }

        System.out.println("Available Slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }
    }

    private static void bookAppointment() {
        String userId = readLine("Enter your user ID: ");
        String userName = readLine("Enter your name: ");
        String userEmail = readLine("Enter your email: ");
        User user = new User(userId, userName, userEmail);

        List<TimeSlot> availableSlots = getAvailableSlots();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for booking.");
            return;
        }

        System.out.println("Choose a slot:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }

        int slotChoice = readInt("Enter slot number: ");
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            System.out.println("Invalid slot selection.");
            return;
        }

        TimeSlot selectedSlot = availableSlots.get(slotChoice - 1);

        int duration = readInt("Enter requested duration in minutes: ");
        int participants = readInt("Enter number of participants: ");

        AppointmentType selectedType = chooseAppointmentType();
        if (selectedType == null) {
            System.out.println("Invalid appointment type.");
            return;
        }

        Appointment appointment = bookingService.bookAppointment(
                user,
                selectedSlot,
                duration,
                participants,
                selectedType
        );

        if (appointment != null) {
            System.out.println("Appointment booked successfully.");
            System.out.println("Status: " + appointment.getStatus());
            System.out.println("Type: " + appointment.getType());
            System.out.println("Slot: " + formatSlot(appointment.getSlot()));
        } else {
            System.out.println("Booking failed. Please check duration, participants, slot availability, or appointment type.");
        }
    }

    private static void viewMyAppointments() {
        String userId = readLine("Enter your user ID: ");
        List<Appointment> appointments = repository.findByUserId(userId);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this user.");
            return;
        }

        printAppointments(appointments);
    }

    private static void modifyMyAppointment() {
        String userId = readLine("Enter your user ID: ");
        List<Appointment> myAppointments = repository.findByUserId(userId);

        if (myAppointments.isEmpty()) {
            System.out.println("No appointments found to modify.");
            return;
        }

        System.out.println("Your appointments:");
        printAppointments(myAppointments);

        int appointmentChoice = readInt("Select appointment number to modify: ");
        if (appointmentChoice < 1 || appointmentChoice > myAppointments.size()) {
            System.out.println("Invalid appointment selection.");
            return;
        }

        Appointment selectedAppointment = myAppointments.get(appointmentChoice - 1);

        List<TimeSlot> availableSlots = getAvailableSlots();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for modification.");
            return;
        }

        System.out.println("Available new slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }

        int slotChoice = readInt("Select new slot number: ");
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            System.out.println("Invalid new slot selection.");
            return;
        }

        TimeSlot newSlot = availableSlots.get(slotChoice - 1);

        Appointment modified = bookingService.modifyAppointment(selectedAppointment, newSlot);

        if (modified != null) {
            System.out.println("Appointment modified successfully.");
            System.out.println("New slot: " + formatSlot(modified.getSlot()));
        } else {
            System.out.println("Modification failed.");
        }
    }

    private static void cancelMyAppointment() {
        String userId = readLine("Enter your user ID: ");
        List<Appointment> myAppointments = repository.findByUserId(userId);

        if (myAppointments.isEmpty()) {
            System.out.println("No appointments found to cancel.");
            return;
        }

        System.out.println("Your appointments:");
        printAppointments(myAppointments);

        int appointmentChoice = readInt("Select appointment number to cancel: ");
        if (appointmentChoice < 1 || appointmentChoice > myAppointments.size()) {
            System.out.println("Invalid appointment selection.");
            return;
        }

        Appointment selectedAppointment = myAppointments.get(appointmentChoice - 1);

        boolean cancelled = bookingService.cancelAppointment(selectedAppointment);

        if (cancelled) {
            System.out.println("Appointment cancelled successfully.");
        } else {
            System.out.println("Cancellation failed.");
        }
    }

    private static void administratorManageReservations() {
        if (!session.isAdminLoggedIn()) {
            System.out.println("Access denied. Administrator login is required.");
            return;
        }

        boolean back = false;

        while (!back) {
            System.out.println("------ Administrator Reservations Menu ------");
            System.out.println("1. View All Reservations");
            System.out.println("2. Modify Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("0. Back");
            System.out.println("---------------------------------------------");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> viewAllReservations();
                case 2 -> adminModifyReservation();
                case 3 -> adminCancelReservation();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }
    }

    private static void viewAllReservations() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        printAppointments(allAppointments);
    }

    private static void adminModifyReservation() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            System.out.println("No reservations available to modify.");
            return;
        }

        printAppointments(allAppointments);

        int appointmentChoice = readInt("Select reservation number to modify: ");
        if (appointmentChoice < 1 || appointmentChoice > allAppointments.size()) {
            System.out.println("Invalid reservation selection.");
            return;
        }

        Appointment selectedAppointment = allAppointments.get(appointmentChoice - 1);

        List<TimeSlot> availableSlots = getAvailableSlots();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for modification.");
            return;
        }

        System.out.println("Available new slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }

        int slotChoice = readInt("Select new slot number: ");
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            System.out.println("Invalid slot selection.");
            return;
        }

        Appointment modified = bookingService.adminModifyAppointment(
                session,
                selectedAppointment,
                availableSlots.get(slotChoice - 1)
        );

        if (modified != null) {
            System.out.println("Reservation modified successfully by administrator.");
        } else {
            System.out.println("Administrator modification failed.");
        }
    }

    private static void adminCancelReservation() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            System.out.println("No reservations available to cancel.");
            return;
        }

        printAppointments(allAppointments);

        int appointmentChoice = readInt("Select reservation number to cancel: ");
        if (appointmentChoice < 1 || appointmentChoice > allAppointments.size()) {
            System.out.println("Invalid reservation selection.");
            return;
        }

        boolean cancelled = bookingService.adminCancelAppointment(
                session,
                allAppointments.get(appointmentChoice - 1)
        );

        if (cancelled) {
            System.out.println("Reservation cancelled successfully by administrator.");
        } else {
            System.out.println("Administrator cancellation failed.");
        }
    }

    private static void sendMockAppointmentReminders() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            System.out.println("No appointments found. No mock reminders sent.");
            return;
        }

        mockReminderService.sendReminders(allAppointments);

        System.out.println("Mock reminder messages generated successfully.");
        System.out.println("Recorded reminder messages:");

        for (String message : mockNotificationService.getSentMessages()) {
            System.out.println("- " + message);
        }
    }

    private static void sendEmailAppointmentReminders() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            System.out.println("No appointments found. No email reminders sent.");
            return;
        }

        emailReminderService.sendReminders(allAppointments);
        System.out.println("Email reminders sent successfully.");
    }

    private static AppointmentType chooseAppointmentType() {
        AppointmentType[] types = AppointmentType.values();

        System.out.println("Appointment Types:");
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }

        int choice = readInt("Select appointment type: ");
        if (choice < 1 || choice > types.length) {
            return null;
        }

        return types[choice - 1];
    }

    private static List<TimeSlot> getAvailableSlots() {
        List<TimeSlot> available = new ArrayList<>();

        for (TimeSlot slot : slots) {
            if (!slot.isBooked() && slot.isFuture(LocalDateTime.now())) {
                available.add(slot);
            }
        }

        return available;
    }

    private static void printAppointments(List<Appointment> appointments) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            System.out.println(
                    (i + 1) + ". User: " + appointment.getUser().getName()
                            + " | ID: " + appointment.getUser().getId()
                            + " | Email: " + appointment.getUser().getEmail()
                            + " | Type: " + appointment.getType()
                            + " | Participants: " + appointment.getParticipantsCount()
                            + " | Status: " + appointment.getStatus()
                            + " | Slot: " + formatSlot(appointment.getSlot())
            );
        }
    }

    private static String formatSlot(TimeSlot slot) {
        return slot.getStart().format(formatter)
                + " | Duration: " + slot.getDurationMinutes() + " min"
                + " | Capacity: " + slot.getMaxParticipants();
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static void seedSlots() {
        LocalDateTime base = LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0).withNano(0);

        slots.add(new TimeSlot(base.withHour(9), 30, 1));
        slots.add(new TimeSlot(base.withHour(10), 60, 2));
        slots.add(new TimeSlot(base.withHour(11), 90, 3));
        slots.add(new TimeSlot(base.withHour(12), 120, 5));
        slots.add(new TimeSlot(base.plusDays(1).withHour(9), 30, 1));
        slots.add(new TimeSlot(base.plusDays(1).withHour(11), 60, 4));
    }
}