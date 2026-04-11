import com.appointment.domain.User;
import com.appointment.service.notification.EmailNotificationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EmailNotificationServiceEdgeTest {

    @Test
    void shouldHandleNullUser() {
        EmailNotificationService service = new EmailNotificationService();
        assertDoesNotThrow(() -> service.notify(null, "test"));
    }

    @Test
    void shouldHandleNullEmail() {
        EmailNotificationService service = new EmailNotificationService();
        User user = new User("1", "Ali", null);

        assertDoesNotThrow(() -> service.notify(user, "test"));
    }

    @Test
    void shouldHandleBlankEmail() {
        EmailNotificationService service = new EmailNotificationService();
        User user = new User("1", "Ali", "   ");

        assertDoesNotThrow(() -> service.notify(user, "test"));
    }

    @Test
    void shouldHandleEmptyMessageWithMissingEmail() {
        EmailNotificationService service = new EmailNotificationService();
        User user = new User("1", "Ali", "");

        assertDoesNotThrow(() -> service.notify(user, ""));
    }
}