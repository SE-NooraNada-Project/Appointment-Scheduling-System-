import com.appointment.domain.User;
import com.appointment.service.notification.EmailNotificationService;
import com.appointment.service.rules.DurationRule;
import com.appointment.service.rules.ParticipantLimitRule;
import com.appointment.domain.TimeSlot;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationAndRulesExtraTest {

    @Test
    void emailNotificationShouldNotThrowWhenUserIsNull() {
        EmailNotificationService service = new EmailNotificationService();

        assertDoesNotThrow(() -> service.notify(null, "test message"));
    }

    @Test
    void emailNotificationShouldNotThrowWhenUserEmailIsNull() {
        EmailNotificationService service = new EmailNotificationService();
        User user = new User("1", "Ali", null);

        assertDoesNotThrow(() -> service.notify(user, "test message"));
    }

    @Test
    void emailNotificationShouldNotThrowWhenUserEmailIsBlank() {
        EmailNotificationService service = new EmailNotificationService();
        User user = new User("1", "Ali", "   ");

        assertDoesNotThrow(() -> service.notify(user, "test message"));
    }

    @Test
    void durationRuleShouldReturnCorrectErrorMessage() {
        DurationRule rule = new DurationRule(60);

        assertEquals("Invalid duration: exceeds maximum allowed minutes", rule.errorMessage());
    }

    @Test
    void participantLimitRuleShouldReturnCorrectErrorMessage() {
        ParticipantLimitRule rule = new ParticipantLimitRule();

        assertEquals("Invalid participants: exceeds slot capacity", rule.errorMessage());
    }

    @Test
    void participantLimitRuleShouldPassAtExactCapacity() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 3);
        ParticipantLimitRule rule = new ParticipantLimitRule();

        assertTrue(rule.isValid(slot, 30, 3));
    }

    @Test
    void durationRuleShouldPassAtExactMaxDuration() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 3);
        DurationRule rule = new DurationRule(30);

        assertTrue(rule.isValid(slot, 30, 1));
    }
}