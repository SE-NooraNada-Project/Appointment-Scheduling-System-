import com.appointment.domain.*;
import com.appointment.service.BookingService;
import org.junit.jupiter.api.Test;
import com.appointment.service.rules.BookingRuleStrategy;
import com.appointment.repository.InMemoryAppointmentRepository;
import java.time.LocalDateTime;
import com.appointment.domain.Administrator;
import com.appointment.domain.Session;
import static org.junit.jupiter.api.Assertions.*;
import com.appointment.repository.AppointmentRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

public class BookingServiceTest {

    @Test
    void shouldBookAppointmentSuccessfully() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
    }

    @Test
    void shouldCancelAppointment() {
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertTrue(result);
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldModifyAppointment() {
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusHours(2), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment modifiedAppointment = service.modifyAppointment(appointment, newSlot);

        assertNotNull(modifiedAppointment);
        assertFalse(oldSlot.isBooked());
        assertTrue(newSlot.isBooked());
        assertEquals(newSlot, modifiedAppointment.getSlot());
        assertEquals(user, modifiedAppointment.getUser());
    }

    @Test
    void shouldAllowAdminToCancel() {
        Administrator admin = new Administrator("1","Admin","admin@test.com","admin","1234");
        Session session = new Session();

        session.login(admin);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.adminCancelAppointment(session, appointment);

        assertTrue(result);
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldFailToModifyAppointmentWhenNewSlotIsBooked() {
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusHours(2), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, oldSlot);
        newSlot.setBooked(true);

        Appointment modifiedAppointment = service.modifyAppointment(appointment, newSlot);

        assertNull(modifiedAppointment);
        assertTrue(oldSlot.isBooked());
    }

    @Test
    void shouldNotAllowAdminCancelWhenNotLoggedIn() {
        Session session = new Session();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();
        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.adminCancelAppointment(session, appointment);

        assertFalse(result);
        assertTrue(slot.isBooked());
    }

    @Test
    void shouldSaveBookedAppointmentInRepository() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
        BookingService service = new BookingService(repository);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldFailBookingWhenSlotAlreadyBooked() {
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        BookingService service = new BookingService();

        service.bookAppointment(user, slot);

        Appointment second = service.bookAppointment(user, slot);

        assertNull(second);
    }

    @Test
    void shouldFailBookingWhenUserIsNull() {
        BookingService service = new BookingService();
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(null, slot);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenSlotIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");

        Appointment appointment = service.bookAppointment(user, null);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenTypeIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot, 30, 1, null);

        assertNull(appointment);
    }

    @Test
    void shouldAllowAdminToModifyWhenLoggedIn() {
        BookingService service = new BookingService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");
        session.login(admin);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment modified = service.adminModifyAppointment(session, appointment, newSlot);

        assertNotNull(modified);
        assertEquals(newSlot, modified.getSlot());
    }
}