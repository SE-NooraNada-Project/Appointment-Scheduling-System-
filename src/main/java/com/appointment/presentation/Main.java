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
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface for the Appointment Scheduling System.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
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
        logger.info("========================================");
        logger.info("      Appointment Scheduling System");
        logger.info("========================================");

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
                    logger.info("Exiting system. Goodbye.");
                }
                default -> logger.info("Invalid choice. Please try again.");
            }

            System.out.println();
        }
    }

    private static void printMainMenu() {
        logger.info("--------------- Main Menu ---------------");
        logger.info("1. Administrator Login");
        logger.info("2. Administrator Logout");
        logger.info("3. View Available Appointment Slots");
        logger.info("4. Book Appointment");
        logger.info("5. View My Appointments");
        logger.info("6. Modify My Appointment");
        logger.info("7. Cancel My Appointment");
        logger.info("8. Administrator Manage Reservations");
        logger.info("9. Send Mock Appointment Reminders");
        logger.info("10. Send Email Appointment Reminders");
        logger.info("0. Exit");
        logger.info("-----------------------------------------");
    }

    private static void administratorLogin() {
        if (session.isAdminLoggedIn()) {
            logger.info("Administrator is already logged in.");
            return;
        }

        String username = readLine("Enter admin username: ");
        String password = readLine("Enter admin password: ");

        boolean success = authService.login(session, admin, username, password);

        if (success) {
            logger.info("Login successful.");
        } else {
            logger.info("Invalid credentials.");
        }
    }

    private static void administratorLogout() {
        if (!session.isAdminLoggedIn()) {
            logger.info("No administrator is currently logged in.");
            return;
        }

        authService.logout(session);
        logger.info("Administrator logged out successfully.");
    }

    private static void viewAvailableSlots() {
        List<TimeSlot> availableSlots = getAvailableSlots();

        if (availableSlots.isEmpty()) {
            logger.info("No available slots found.");
            return;
        }

        logger.info("Available Slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            logger.info((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }
    }

    private static void bookAppointment() {
        String userId = readLine("Enter your user ID: ");
        String userName = readLine("Enter your name: ");
        String userEmail = readLine("Enter your email: ");
        User user = new User(userId, userName, userEmail);

        List<TimeSlot> availableSlots = getAvailableSlots();
        if (availableSlots.isEmpty()) {
            logger.info("No available slots for booking.");
            return;
        }

        logger.info("Choose a slot:");
        for (int i = 0; i < availableSlots.size(); i++) {
            logger.info((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }

        int slotChoice = readInt("Enter slot number: ");
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            logger.info("Invalid slot selection.");
            return;
        }

        TimeSlot selectedSlot = availableSlots.get(slotChoice - 1);

        int duration = readInt("Enter requested duration in minutes: ");
        int participants = readInt("Enter number of participants: ");

        AppointmentType selectedType = chooseAppointmentType();
        if (selectedType == null) {
            logger.info("Invalid appointment type.");
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
            logger.info("Appointment booked successfully.");
            logger.info("Status: " + appointment.getStatus());
            logger.info("Type: " + appointment.getType());
            logger.info("Slot: " + formatSlot(appointment.getSlot()));
        } else {
            logger.info("Booking failed. Please check duration, participants, slot availability, or appointment type.");
        }
    }

    private static void viewMyAppointments() {
        String userId = readLine("Enter your user ID: ");
        List<Appointment> appointments = repository.findByUserId(userId);

        if (appointments.isEmpty()) {
            logger.info("No appointments found for this user.");
            return;
        }

        printAppointments(appointments);
    }

    private static void modifyMyAppointment() {
        String userId = readLine("Enter your user ID: ");
        List<Appointment> myAppointments = repository.findByUserId(userId);

        if (myAppointments.isEmpty()) {
            logger.info("No appointments found to modify.");
            return;
        }

        logger.info("Your appointments:");
        printAppointments(myAppointments);

        int appointmentChoice = readInt("Select appointment number to modify: ");
        if (appointmentChoice < 1 || appointmentChoice > myAppointments.size()) {
            logger.info("Invalid appointment selection.");
            return;
        }

        Appointment selectedAppointment = myAppointments.get(appointmentChoice - 1);

        List<TimeSlot> availableSlots = getAvailableSlots();
        if (availableSlots.isEmpty()) {
            logger.info("No available slots for modification.");
            return;
        }

        logger.info("Available new slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            logger.info((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }

        int slotChoice = readInt("Select new slot number: ");
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            logger.info("Invalid new slot selection.");
            return;
        }

        TimeSlot newSlot = availableSlots.get(slotChoice - 1);

        Appointment modified = bookingService.modifyAppointment(selectedAppointment, newSlot);

        if (modified != null) {
            logger.info("Appointment modified successfully.");
            logger.info("New slot: " + formatSlot(modified.getSlot()));
        } else {
            logger.info("Modification failed.");
        }
    }

    private static void cancelMyAppointment() {
        String userId = readLine("Enter your user ID: ");
        List<Appointment> myAppointments = repository.findByUserId(userId);

        if (myAppointments.isEmpty()) {
            logger.info("No appointments found to cancel.");
            return;
        }

        logger.info("Your appointments:");
        printAppointments(myAppointments);

        int appointmentChoice = readInt("Select appointment number to cancel: ");
        if (appointmentChoice < 1 || appointmentChoice > myAppointments.size()) {
            logger.info("Invalid appointment selection.");
            return;
        }

        Appointment selectedAppointment = myAppointments.get(appointmentChoice - 1);

        boolean cancelled = bookingService.cancelAppointment(selectedAppointment);

        if (cancelled) {
            logger.info("Appointment cancelled successfully.");
        } else {
            logger.info("Cancellation failed.");
        }
    }

    private static void administratorManageReservations() {
        if (!session.isAdminLoggedIn()) {
            logger.info("Access denied. Administrator login is required.");
            return;
        }

        boolean back = false;

        while (!back) {
            logger.info("------ Administrator Reservations Menu ------");
            logger.info("1. View All Reservations");
            logger.info("2. Modify Reservation");
            logger.info("3. Cancel Reservation");
            logger.info("4. Add Appointment Slot");
            logger.info("5. Delete Appointment Slot");
            logger.info("0. Back");
            logger.info("---------------------------------------------");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> viewAllReservations();
                case 2 -> adminModifyReservation();
                case 3 -> adminCancelReservation();
                case 4 -> adminAddAppointmentSlot();
                case 5 -> adminDeleteAppointmentSlot();
                case 0 -> back = true;
                default -> logger.info("Invalid choice. Please try again.");
            }

            System.out.println();
        }
    }

    private static void viewAllReservations() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            logger.info("No reservations found.");
            return;
        }

        printAppointments(allAppointments);
    }

    private static void adminModifyReservation() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            logger.info("No reservations available to modify.");
            return;
        }

        printAppointments(allAppointments);

        int appointmentChoice = readInt("Select reservation number to modify: ");
        if (appointmentChoice < 1 || appointmentChoice > allAppointments.size()) {
            logger.info("Invalid reservation selection.");
            return;
        }

        Appointment selectedAppointment = allAppointments.get(appointmentChoice - 1);

        List<TimeSlot> availableSlots = getAvailableSlots();
        if (availableSlots.isEmpty()) {
            logger.info("No available slots for modification.");
            return;
        }

        logger.info("Available new slots:");
        for (int i = 0; i < availableSlots.size(); i++) {
            logger.info((i + 1) + ". " + formatSlot(availableSlots.get(i)));
        }

        int slotChoice = readInt("Select new slot number: ");
        if (slotChoice < 1 || slotChoice > availableSlots.size()) {
            logger.info("Invalid slot selection.");
            return;
        }

        Appointment modified = bookingService.adminModifyAppointment(
                session,
                selectedAppointment,
                availableSlots.get(slotChoice - 1)
        );

        if (modified != null) {
            logger.info("Reservation modified successfully by administrator.");
        } else {
            logger.info("Administrator modification failed.");
        }
    }

    private static void adminCancelReservation() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            logger.info("No reservations available to cancel.");
            return;
        }

        printAppointments(allAppointments);

        int appointmentChoice = readInt("Select reservation number to cancel: ");
        if (appointmentChoice < 1 || appointmentChoice > allAppointments.size()) {
            logger.info("Invalid reservation selection.");
            return;
        }

        boolean cancelled = bookingService.adminCancelAppointment(
                session,
                allAppointments.get(appointmentChoice - 1)
        );

        if (cancelled) {
            logger.info("Reservation cancelled successfully by administrator.");
        } else {
            logger.info("Administrator cancellation failed.");
        }
    }
    private static void adminAddAppointmentSlot() {
        logger.info("Add New Appointment Slot");

        String dateTimeInput = readLine("Enter slot date and time (yyyy-MM-dd HH:mm): ");
        int duration = readInt("Enter duration in minutes: ");
        int capacity = readInt("Enter maximum participants: ");

        try {
            LocalDateTime start = LocalDateTime.parse(dateTimeInput, formatter);

            if (!start.isAfter(LocalDateTime.now())) {
                logger.info("Slot must be in the future.");
                return;
            }

            if (duration <= 0 || capacity <= 0) {
                logger.info("Duration and capacity must be greater than zero.");
                return;
            }

            for (TimeSlot slot : slots) {
                if (slot.getStart().equals(start)) {
                    logger.info("A slot already exists at this time.");
                    return;
                }
            }

            TimeSlot newSlot = new TimeSlot(start, duration, capacity);
            slots.add(newSlot);

            logger.info("Appointment slot added successfully.");
            logger.info("New slot: " + formatSlot(newSlot));

        } catch (Exception e) {
            logger.info("Invalid date/time format. Please use yyyy-MM-dd HH:mm");
        }
    }
    private static void adminDeleteAppointmentSlot() {
        List<TimeSlot> futureSlots = new ArrayList<>();

        for (TimeSlot slot : slots) {
            if (slot.isFuture(LocalDateTime.now())) {
                futureSlots.add(slot);
            }
        }

        if (futureSlots.isEmpty()) {
            logger.info("No future slots available to delete.");
            return;
        }

        logger.info("Future slots:");
        for (int i = 0; i < futureSlots.size(); i++) {
            TimeSlot slot = futureSlots.get(i);
            String status = slot.isBooked() ? "BOOKED" : "AVAILABLE";
            logger.info((i + 1) + ". " + formatSlot(slot) + " | Status: " + status);
        }

        int choice = readInt("Select slot number to delete: ");
        if (choice < 1 || choice > futureSlots.size()) {
            logger.info("Invalid slot selection.");
            return;
        }

        TimeSlot selectedSlot = futureSlots.get(choice - 1);

        if (selectedSlot.isBooked()) {
            logger.info("Cannot delete a booked slot.");
            return;
        }

        boolean removed = slots.remove(selectedSlot);

        if (removed) {
            logger.info("Appointment slot deleted successfully.");
        } else {
            logger.info("Deletion failed.");
        }
    }
    private static void sendMockAppointmentReminders() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            logger.info("No appointments found. No mock reminders sent.");
            return;
        }

        mockReminderService.sendReminders(allAppointments);

        logger.info("Mock reminder messages generated successfully.");
        logger.info("Recorded reminder messages:");

        for (String message : mockNotificationService.getSentMessages()) {
            logger.info("- " + message);
        }
    }

    private static void sendEmailAppointmentReminders() {
        List<Appointment> allAppointments = repository.findAll();

        if (allAppointments.isEmpty()) {
            logger.info("No appointments found. No email reminders sent.");
            return;
        }

        emailReminderService.sendReminders(allAppointments);
        logger.info("Email reminders sent successfully.");
    }

    private static AppointmentType chooseAppointmentType() {
        AppointmentType[] types = AppointmentType.values();

        logger.info("Appointment Types:");
        for (int i = 0; i < types.length; i++) {
            logger.info((i + 1) + ". " + types[i]);
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
            logger.info(
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
                logger.info("Please enter a valid number.");
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