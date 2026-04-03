import com.appointment.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTypeTest {

    @Test
    void shouldCreateAppointmentWithType() {

        User user = new User("1","Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now(),30);

        Appointment appointment = new Appointment(
                user,
                slot,
                1,
                AppointmentType.GROUP
        );

        assertEquals(AppointmentType.GROUP, appointment.getType());
    }
}