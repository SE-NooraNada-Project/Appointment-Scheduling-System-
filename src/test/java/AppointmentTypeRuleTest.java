import com.appointment.domain.AppointmentType;
import com.appointment.service.rules.AppointmentTypeRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTypeRuleTest {

    @Test
    void shouldFailIndividualWhenParticipantsMoreThanOne() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.INDIVIDUAL, 2);

        assertFalse(result);
    }

    @Test
    void shouldPassIndividualWhenOneParticipant() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.INDIVIDUAL, 1);

        assertTrue(result);
    }

    @Test
    void shouldFailGroupWhenOnlyOneParticipant() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.GROUP, 1);

        assertFalse(result);
    }

    @Test
    void shouldPassGroupWhenParticipantsMoreThanOne() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.GROUP, 3);

        assertTrue(result);
    }

    @Test
    void shouldPassAssessmentWhenOneParticipant() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.ASSESSMENT, 1);

        assertTrue(result);
    }

    @Test
    void shouldFailAssessmentWhenParticipantsMoreThanOne() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.ASSESSMENT, 2);

        assertFalse(result);
    }

    @Test
    void shouldPassFollowUpWhenOneParticipant() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.FOLLOW_UP, 1);

        assertTrue(result);
    }

    @Test
    void shouldFailFollowUpWhenParticipantsMoreThanOne() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.FOLLOW_UP, 2);

        assertFalse(result);
    }

    @Test
    void shouldPassOtherTypesWhenParticipantsGreaterThanZero() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.VIRTUAL, 2);

        assertTrue(result);
    }

    @Test
    void shouldFailOtherTypesWhenParticipantsZero() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        boolean result = rule.isValid(AppointmentType.VIRTUAL, 0);

        assertFalse(result);
    }

    @Test
    void shouldReturnErrorMessage() {
        AppointmentTypeRule rule = new AppointmentTypeRule();

        assertEquals("Invalid participants count for appointment type", rule.errorMessage());
    }
}