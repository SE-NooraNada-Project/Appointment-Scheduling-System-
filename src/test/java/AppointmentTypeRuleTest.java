import com.appointment.domain.Appointment;
import com.appointment.domain.AppointmentType;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.BookingService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTypeRuleTest {

    @Test
    void shouldAllowIndividualAppointmentWithOneParticipant() {
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot, 30, 1, AppointmentType.INDIVIDUAL);

        assertNotNull(appointment);
        assertEquals(AppointmentType.INDIVIDUAL, appointment.getType());
    }

    @Test
    void shouldRejectIndividualAppointmentWithMoreThanOneParticipant() {
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot, 30, 2, AppointmentType.INDIVIDUAL);

        assertNull(appointment);
    }

    @Test
    void shouldAllowGroupAppointmentWithMoreThanOneParticipant() {
        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30, 5);

        BookingService service = new BookingService();

        Appointment appointment = service.bookAppointment(user, slot, 30, 3, AppointmentType.GROUP);

        assertNotNull(appointment);
        assertEquals(AppointmentType.GROUP, appointment.getType());
    }
}