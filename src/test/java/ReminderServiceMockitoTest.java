import com.appointment.domain.Appointment;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.notification.Observer;
import com.appointment.service.notification.ReminderService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReminderServiceMockitoTest {

    @Test
    void shouldSendReminderUsingMockito() {

        Observer mockObserver = mock(Observer.class);

        ReminderService reminderService = new ReminderService(mockObserver);

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusHours(1), 30);
        Appointment appointment = new Appointment(user, slot);

        reminderService.sendReminders(List.of(appointment));

        verify(mockObserver, times(1))
                .notify(eq(user), contains("Reminder"));
    }
}