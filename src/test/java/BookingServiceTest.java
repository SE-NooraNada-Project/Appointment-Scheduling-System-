import com.appointment.domain.*;
import com.appointment.service.BookingService;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import com.appointment.domain.Administrator;
import com.appointment.domain.Session;
import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {

    @Test
    void shouldBookAppointmentSuccessfully() {

        User user = new User("1","Noora");
        TimeSlot slot = new TimeSlot(LocalDateTime.now(),30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user,slot);

        assertNotNull(appointment);
        assertTrue(slot.isBooked());
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

        boolean result = service.modifyAppointment(appointment, newSlot);

        assertTrue(result);
        assertFalse(oldSlot.isBooked());
        assertTrue(newSlot.isBooked());
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
}