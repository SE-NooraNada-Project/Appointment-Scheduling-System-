import com.appointment.domain.TimeSlot;
import com.appointment.service.rules.DurationRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class DurationRuleTest {

    @Test
    void shouldFailWhenDurationTooLong() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        DurationRule rule = new DurationRule(30);

        boolean result = rule.isValid(slot, 60, 1);

        assertFalse(result);
    }

    @Test
    void shouldPassWhenDurationValid() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);

        DurationRule rule = new DurationRule(60);

        boolean result = rule.isValid(slot, 30, 1);

        assertTrue(result);
    }
}