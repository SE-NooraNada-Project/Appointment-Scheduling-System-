import com.appointment.domain.TimeSlot;
import com.appointment.service.rules.ParticipantLimitRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class ParticipantLimitRuleTest {

    @Test
    void shouldFailWhenParticipantsExceedLimit() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 2);

        ParticipantLimitRule rule = new ParticipantLimitRule();

        boolean result = rule.isValid(slot, 30, 3);

        assertFalse(result);
    }

    @Test
    void shouldPassWhenParticipantsWithinLimit() {
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30, 3);

        ParticipantLimitRule rule = new ParticipantLimitRule();

        boolean result = rule.isValid(slot, 30, 2);

        assertTrue(result);
    }
}