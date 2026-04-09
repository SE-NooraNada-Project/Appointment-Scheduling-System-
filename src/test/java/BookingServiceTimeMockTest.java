import com.appointment.domain.Appointment;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.BookingService;
import com.appointment.service.time.TimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTimeMockTest {

    @Test
    void shouldBookAppointmentUsingMockedCurrentTime() {
        TimeProvider timeProvider = mock(TimeProvider.class);

        LocalDateTime mockedNow = LocalDateTime.of(2025, 1, 1, 10, 0);
        when(timeProvider.now()).thenReturn(mockedNow);

        BookingService service = new BookingService(null, null, timeProvider);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(mockedNow.plusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNotNull(appointment);
        assertTrue(slot.isBooked());
        verify(timeProvider, atLeastOnce()).now();
    }

    @Test
    void shouldRejectPastSlotUsingMockedCurrentTime() {
        TimeProvider timeProvider = mock(TimeProvider.class);

        LocalDateTime mockedNow = LocalDateTime.of(2025, 1, 10, 10, 0);
        when(timeProvider.now()).thenReturn(mockedNow);

        BookingService service = new BookingService(null, null, timeProvider);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(mockedNow.minusDays(1), 30);

        Appointment appointment = service.bookAppointment(user, slot);

        assertNull(appointment);
        verify(timeProvider, atLeastOnce()).now();
    }
}