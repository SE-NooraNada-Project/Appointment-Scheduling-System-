import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.BookingService;
import com.appointment.service.rules.DurationRule;
import com.appointment.service.rules.ParticipantLimitRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingRulesTest {

    @Test
    void shouldRejectTooLongDuration() {
        BookingService service = new BookingService(List.of(
                new DurationRule(30),
                new ParticipantLimitRule()
        ));

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now(), 30, 5);

        // طلب 60 دقيقة > 30
        assertNull(service.bookAppointment(user, slot, 60, 1));
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldRejectTooManyParticipants() {
        BookingService service = new BookingService(List.of(
                new DurationRule(30),
                new ParticipantLimitRule()
        ));

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now(), 30, 2);

        // طلب 3 مشاركين > سعة 2
        assertNull(service.bookAppointment(user, slot, 30, 3));
        assertFalse(slot.isBooked());
    }

    @Test
    void shouldAcceptValidBooking() {
        BookingService service = new BookingService(List.of(
                new DurationRule(30),
                new ParticipantLimitRule()
        ));

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now(), 30, 3);

        var appointment = service.bookAppointment(user, slot, 30, 2);

        assertNotNull(appointment);
        assertTrue(slot.isBooked());
        assertEquals("CONFIRMED", appointment.getStatus());
        assertEquals(2, appointment.getParticipantsCount());
    }
}