import com.appointment.domain.Appointment;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.notification.MockNotificationService;
import com.appointment.service.notification.ReminderService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReminderServiceTest {

    @Test
    void shouldSendReminder() {
        User user = new User("1", "Ali");

        TimeSlot slot = new TimeSlot(
                LocalDateTime.now().plusHours(1),
                30
        );

        Appointment appointment = new Appointment(user, slot);

        MockNotificationService mockService = new MockNotificationService();
        ReminderService reminderService = new ReminderService(mockService);

        reminderService.sendReminders(List.of(appointment));

        assertEquals(1, mockService.getSentMessages().size());
        assertTrue(mockService.getSentMessages().get(0).contains("Reminder"));
        assertTrue(mockService.getSentMessages().get(0).contains("Ali"));
    }

}