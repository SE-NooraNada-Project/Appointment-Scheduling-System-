import com.appointment.domain.*;
import com.appointment.service.BookingService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
}