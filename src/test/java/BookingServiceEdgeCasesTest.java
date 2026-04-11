import com.appointment.domain.*;
import com.appointment.repository.AppointmentRepository;
import com.appointment.repository.InMemoryAppointmentRepository;
import com.appointment.service.BookingService;
import com.appointment.service.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceEdgeCasesTest {

    @Test
    void shouldFailBookingWhenSlotIsInPastWithDetailedMethod() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot, 30, 1);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenUserIsNullInDetailedMethod() {
        BookingService service = new BookingService();
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(null, slot, 30, 1);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenAppointmentTypeDoesNotMatchParticipants() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 5);

        Appointment appointment = service.bookAppointment(user, slot, 30, 2, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldFailCancelWhenAppointmentIsNull() {
        BookingService service = new BookingService();

        boolean result = service.cancelAppointment(null);

        assertFalse(result);
    }

    @Test
    void shouldFailCancelWhenSlotIsNotBooked() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = new Appointment(user, slot);
        slot.setBooked(false); // نرجّعها unbooked حتى نفوت هذا الفرع

        boolean result = service.cancelAppointment(appointment);

        assertFalse(result);
    }

    @Test
    void shouldDeleteAppointmentFromRepositoryWhenCancelled() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
        BookingService service = new BookingService(repository);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertTrue(result);
        assertTrue(repository.findAll().isEmpty());
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldFailModifyWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment result = service.modifyAppointment(null, newSlot);

        assertNull(result);
    }

    @Test
    void shouldFailModifyWhenNewSlotIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment result = service.modifyAppointment(appointment, null);

        assertNull(result);
        assertTrue(oldSlot.isBooked());
    }

    @Test
    void shouldFailModifyWhenOldSlotIsInPast() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = new Appointment(user, oldSlot);

        Appointment result = service.modifyAppointment(appointment, newSlot);

        assertNull(result);
    }

    @Test
    void shouldFailModifyWhenOldSlotIsNotBooked() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment appointment = new Appointment(user, oldSlot);
        oldSlot.setBooked(false);

        Appointment result = service.modifyAppointment(appointment, newSlot);

        assertNull(result);
        assertFalse(oldSlot.isBooked());
        assertFalse(newSlot.isBooked());
    }

    @Test
    void shouldUpdateRepositoryWhenAppointmentModified() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
        BookingService service = new BookingService(repository);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment original = service.bookAppointment(user, oldSlot);
        Appointment modified = service.modifyAppointment(original, newSlot);

        assertNotNull(modified);
        assertEquals(1, repository.findAll().size());
        assertEquals(newSlot, repository.findAll().get(0).getSlot());
        assertFalse(oldSlot.isBooked());
        assertTrue(newSlot.isBooked());
    }

    @Test
    void shouldFailAdminCancelWhenSessionIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.adminCancelAppointment(null, appointment);

        assertFalse(result);
        assertTrue(slot.isBooked());
    }

    @Test
    void shouldFailAdminCancelWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        Session session = new Session();

        boolean result = service.adminCancelAppointment(session, null);

        assertFalse(result);
    }

    @Test
    void shouldFailAdminModifyWhenSessionIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment result = service.adminModifyAppointment(null, appointment, newSlot);

        assertNull(result);
    }

    @Test
    void shouldFailAdminModifyWhenAdminNotLoggedIn() {
        BookingService service = new BookingService();
        Session session = new Session();

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment result = service.adminModifyAppointment(session, appointment, newSlot);

        assertNull(result);
    }

    @Test
    void shouldConstructBookingServiceWithNullRulesWithoutCrashing() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.now()).thenReturn(LocalDateTime.now());

        assertDoesNotThrow(() -> new BookingService((List) null, repository, timeProvider));
        assertDoesNotThrow(() -> new BookingService((List) null, timeProvider));
    }
}