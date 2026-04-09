import com.appointment.domain.Administrator;
import com.appointment.domain.Appointment;
import com.appointment.domain.Session;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.repository.InMemoryAppointmentRepository;
import com.appointment.service.BookingService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceEdgeCasesTest {

    @Test
    void shouldFailCancelWhenAppointmentIsNull() {
        BookingService service = new BookingService();

        assertFalse(service.cancelAppointment(null));
    }

    @Test
    void shouldFailCancelWhenSlotIsNotBooked() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = new Appointment(user, slot);

        slot.setBooked(false);

        assertFalse(service.cancelAppointment(appointment));
    }

    @Test
    void shouldFailModifyWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        assertNull(service.modifyAppointment(null, newSlot));
    }

    @Test
    void shouldFailModifyWhenNewSlotIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        assertNull(service.modifyAppointment(appointment, null));
    }

    @Test
    void shouldFailModifyWhenOldSlotIsInPast() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = new Appointment(user, oldSlot);

        assertNull(service.modifyAppointment(appointment, newSlot));
    }

    @Test
    void shouldFailAdminCancelWhenSessionIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, slot);

        assertFalse(service.adminCancelAppointment(null, appointment));
    }

    @Test
    void shouldFailAdminCancelWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");
        session.login(admin);

        assertFalse(service.adminCancelAppointment(session, null));
    }

    @Test
    void shouldFailAdminModifyWhenSessionIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        assertNull(service.adminModifyAppointment(null, appointment, newSlot));
    }

    @Test
    void shouldFailAdminModifyWhenAdminNotLoggedIn() {
        BookingService service = new BookingService();
        Session session = new Session();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        assertNull(service.adminModifyAppointment(session, appointment, newSlot));
    }

    @Test
    void shouldDeleteFromRepositoryWhenCancelling() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
        BookingService service = new BookingService(repository);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);
        boolean cancelled = service.cancelAppointment(appointment);

        assertTrue(cancelled);
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReplaceAppointmentInRepositoryWhenModifying() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
        BookingService service = new BookingService(repository);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment appointment = service.bookAppointment(user, oldSlot);
        Appointment modified = service.modifyAppointment(appointment, newSlot);

        assertNotNull(modified);
        assertEquals(1, repository.findAll().size());
        assertEquals(newSlot, repository.findAll().get(0).getSlot());
    }
}