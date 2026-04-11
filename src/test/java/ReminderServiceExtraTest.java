import com.appointment.domain.Appointment;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.service.notification.MockNotificationService;
import com.appointment.service.notification.ReminderService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReminderServiceExtraTest {

    @Test
    void shouldHandleEmptyAppointmentsList() {
        MockNotificationService mockService = new MockNotificationService();
        ReminderService reminderService = new ReminderService(mockService);

        reminderService.sendReminders(List.of());

        assertTrue(mockService.getSentMessages().isEmpty());
    }

    @Test
    void shouldSendMultipleReminders() {
        MockNotificationService mockService = new MockNotificationService();
        ReminderService reminderService = new ReminderService(mockService);

        User user1 = new User("1", "Ali", "ali@test.com");
        User user2 = new User("2", "Sara", "sara@test.com");

        Appointment a1 = new Appointment(user1, new TimeSlot(LocalDateTime.now().plusDays(1), 30));
        Appointment a2 = new Appointment(user2, new TimeSlot(LocalDateTime.now().plusDays(2), 30));

        reminderService.sendReminders(List.of(a1, a2));

        assertEquals(2, mockService.getSentMessages().size());
    }
}