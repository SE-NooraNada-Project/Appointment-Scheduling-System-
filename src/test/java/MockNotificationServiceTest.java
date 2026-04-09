import com.appointment.domain.User;
import com.appointment.service.notification.MockNotificationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MockNotificationServiceTest {

    @Test
    void shouldStoreSentMessage() {
        MockNotificationService service = new MockNotificationService();
        User user = new User("1", "Ali", "ali@test.com");

        service.notify(user, "Reminder message");

        assertEquals(1, service.getSentMessages().size());
        assertEquals("Ali: Reminder message", service.getSentMessages().get(0));
    }
}