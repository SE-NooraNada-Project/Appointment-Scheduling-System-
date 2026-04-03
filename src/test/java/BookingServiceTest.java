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
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
    }

    @Test
    void shouldCancelAppointment() {

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertTrue(result);
        assertFalse(slot.isBooked());
    }
    @Test
    void shouldModifyAppointment() {

        User user = new User("1", "Ali");

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

        Administrator admin = new Administrator("1","Admin","admin","1234");
        Session session = new Session();

        session.login(admin);

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.adminCancelAppointment(session, appointment);

        assertTrue(result);
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldFailToModifyAppointmentWhenNewSlotIsBooked() {

        User user = new User("1", "Ali");
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
    void shouldFailToModifyWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusHours(2), 30);

        Appointment modifiedAppointment = service.modifyAppointment(null, newSlot);

        assertNull(modifiedAppointment);
    }

    @Test
    void shouldFailToCancelNullAppointment() {
        BookingService service = new BookingService();

        boolean result = service.cancelAppointment(null);

        assertFalse(result);
    }

    @Test
    void shouldNotAllowAdminCancelWhenNotLoggedIn() {

        Session session = new Session();
        User user = new User("1", "Ali");
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

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldFailBookingWhenSlotAlreadyBooked() {
        User user = new User("1", "Ali");
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
        User user = new User("1", "Ali");

        Appointment appointment = service.bookAppointment(user, null);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenTypeIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot, 30, 1, null);

        assertNull(appointment);
    }

    @Test
    void shouldFailModifyWhenNewSlotIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment modified = service.modifyAppointment(appointment, null);

        assertNull(modified);
    }

    @Test
    void shouldFailAdminModifyWhenSessionIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment modified = service.adminModifyAppointment(null, appointment, newSlot);

        assertNull(modified);
    }

    @Test
    void shouldFailBookingWhenSlotAlreadyBookedAgain() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        service.bookAppointment(user, slot);
        Appointment secondAppointment = service.bookAppointment(user, slot);

        assertNull(secondAppointment);
    }

    @Test
    void shouldFailBookingWhenSlotIsInPast() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNull(appointment);
    }

    @Test
    void shouldFailAdminModifyWhenNotLoggedIn() {
        BookingService service = new BookingService();
        Session session = new Session();
        User user = new User("1", "Ali");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment appointment = service.bookAppointment(user, oldSlot);
        Appointment modified = service.adminModifyAppointment(session, appointment, newSlot);

        assertNull(modified);
        assertTrue(oldSlot.isBooked());
        assertFalse(newSlot.isBooked());
    }

    @Test
    void shouldFailAdminModifyWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin", "1234");
        session.login(admin);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment modified = service.adminModifyAppointment(session, null, newSlot);

        assertNull(modified);
    }


    @Test
    void shouldSaveAppointmentWhenRepositoryExists() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        BookingService service = new BookingService(repository);
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
        verify(repository).save(any(Appointment.class));
    }

    @Test
    void shouldUpdateRepositoryWhenModifySucceeds() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        BookingService service = new BookingService(repository);

        User user = new User("1", "Ali");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        oldSlot.setBooked(true);
        Appointment appointment = new Appointment(user, oldSlot, 1, AppointmentType.INDIVIDUAL);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment modified = service.modifyAppointment(appointment, newSlot);

        assertNotNull(modified);
        verify(repository).delete(appointment);
        verify(repository).save(any(Appointment.class));
    }

    @Test
    void shouldFailAdminCancelWhenSessionIsNull() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        slot.setBooked(true);
        Appointment appointment = new Appointment(user, slot, 1, AppointmentType.INDIVIDUAL);

        boolean result = service.adminCancelAppointment(null, appointment);

        assertFalse(result);
    }

    @Test
    void shouldFailAdminCancelWhenAppointmentIsNull() {
        BookingService service = new BookingService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin", "1234");
        session.login(admin);

        boolean result = service.adminCancelAppointment(session, null);

        assertFalse(result);
    }

    @Test
    void shouldFailBookingWhenSlotAlreadyBookedDirect() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        slot.setBooked(true);

        Appointment appointment = service.bookAppointment(user, slot, 30, 1, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenParticipantsMismatchTypeEdge() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 5);

        Appointment appointment = service.bookAppointment(user, slot, 30, 2, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
    }

//    @Test
//    void shouldFailModifyWhenOldSlotNotBooked() {
//        BookingService service = new BookingService();
//        User user = new User("1", "Ali");
//
//        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
//        // NOT booked
//
//        Appointment appointment = new Appointment(user, oldSlot, 1, AppointmentType.INDIVIDUAL);
//        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);
//
//        Appointment modified = service.modifyAppointment(appointment, newSlot);
//
//        assertNull(modified);
//    }

    @Test
    void shouldFailModifyWhenNewSlotAlreadyBooked() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        oldSlot.setBooked(true);

        Appointment appointment = new Appointment(user, oldSlot, 1, AppointmentType.INDIVIDUAL);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);
        newSlot.setBooked(true);

        Appointment modified = service.modifyAppointment(appointment, newSlot);

        assertNull(modified);
    }

    @Test
    void shouldFailBookingWhenUserIsNullFullMethod() {
        BookingService service = new BookingService();
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(null, slot, 30, 1, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenParticipantsZero() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot, 30, 0, AppointmentType.GROUP);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenUserIsNullInFullMethod() {
        BookingService service = new BookingService();
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment =
                service.bookAppointment(null, slot, 30, 1, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenSlotIsNullInFullMethod() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");

        Appointment appointment =
                service.bookAppointment(user, null, 30, 1, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
    }

    @Test
    void shouldFailBookingWhenParticipantsZeroForGroup() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 5);

        Appointment appointment =
                service.bookAppointment(user, slot, 30, 0, AppointmentType.GROUP);

        assertNull(appointment);
    }

    @Test
    void shouldDeleteAppointmentWhenRepositoryExists() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        BookingService service = new BookingService(repository);
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertTrue(result);
        verify(repository).delete(appointment);
    }

    @Test
    void shouldFailBookingWhenTypeIsNullEdge() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment =
                service.bookAppointment(user, slot, 30, 1, null);

        assertNull(appointment);
    }

    @Test
    void shouldBookWithoutRepository() {
        BookingService service = new BookingService(); // repository = null
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
    }

    @Test
    void shouldCancelWithoutRepository() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertTrue(result);
    }

    @Test
    void shouldBookSuccessfullyWhenCustomRulePasses() {
        BookingRuleStrategy rule = new BookingRuleStrategy() {
            @Override
            public boolean isValid(TimeSlot slot, int requestedDurationMinutes, int participantsCount) {
                return true;
            }

            @Override
            public String errorMessage() {
                return "ok";
            }
        };

        BookingService service = new BookingService(java.util.List.of(rule));
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment =
                service.bookAppointment(user, slot, 30, 1, AppointmentType.INDIVIDUAL);

        assertNotNull(appointment);
    }

    @Test
    void shouldFailCancelWhenSlotIsNotBookedAfterAppointmentCreation() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);
        slot.setBooked(false);

        boolean result = service.cancelAppointment(appointment);

        assertFalse(result);
    }

    @Test
    void shouldFailCancelWhenAppointmentSlotIsInPast() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);
        slot.setBooked(true);
        Appointment appointment = new Appointment(user, slot, 1, AppointmentType.INDIVIDUAL);

        boolean result = service.cancelAppointment(appointment);

        assertFalse(result);
    }

    @Test
    void shouldFailModifyWhenOldSlotIsInPast() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);
        oldSlot.setBooked(true);
        Appointment appointment = new Appointment(user, oldSlot, 1, AppointmentType.INDIVIDUAL);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment modified = service.modifyAppointment(appointment, newSlot);

        assertNull(modified);
    }

    @Test
    void shouldFailModifyWhenOldSlotBecomesNotBooked() {
        BookingService service = new BookingService();
        User user = new User("1", "Ali");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);
        oldSlot.setBooked(false);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment modified = service.modifyAppointment(appointment, newSlot);

        assertNull(modified);
    }

    @Test
    void shouldAllowAdminToModifyWhenLoggedIn() {
        BookingService service = new BookingService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin", "1234");
        session.login(admin);

        User user = new User("1", "Ali");
        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = service.bookAppointment(user, oldSlot);

        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment modified = service.adminModifyAppointment(session, appointment, newSlot);

        assertNotNull(modified);
        assertEquals(newSlot, modified.getSlot());
    }

}