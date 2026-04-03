import com.appointment.domain.*;
import com.appointment.service.BookingService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentModificationTest {

    @Test
    void shouldCancelFutureAppointment() {

        BookingService service = new BookingService();

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertTrue(result);
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldNotCancelPastAppointment() {

        BookingService service = new BookingService();

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().minusDays(1), 30);

        Appointment appointment = new Appointment(user, slot);

        boolean result = service.cancelAppointment(appointment);

        assertFalse(result);
    }

    @Test
    void shouldModifyFutureAppointment() {

        BookingService service = new BookingService();

        User user = new User("1", "Ali");

        TimeSlot oldSlot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot newSlot = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        Appointment appointment = service.bookAppointment(user, oldSlot);

        Appointment modifiedAppointment = service.modifyAppointment(appointment, newSlot);

        assertNotNull(modifiedAppointment);
        assertFalse(oldSlot.isBooked());
        assertTrue(newSlot.isBooked());
        assertEquals(newSlot, modifiedAppointment.getSlot());
        assertEquals(appointment.getUser(), modifiedAppointment.getUser());
        assertEquals(appointment.getType(), modifiedAppointment.getType());
        assertEquals(appointment.getParticipantsCount(), modifiedAppointment.getParticipantsCount());
        
        assertFalse(oldSlot.isBooked());
        assertTrue(newSlot.isBooked());
    }
}