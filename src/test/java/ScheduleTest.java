import com.appointment.domain.Schedule;
import com.appointment.domain.TimeSlot;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {

    @Test
    void shouldReturnOnlyAvailableSlots(){

        Schedule schedule = new Schedule();

        TimeSlot s1 = new TimeSlot(LocalDateTime.now(),30);
        TimeSlot s2 = new TimeSlot(LocalDateTime.now().plusHours(1),30);

        s1.setBooked(true);

        schedule.addSlot(s1);
        schedule.addSlot(s2);

        assertEquals(1, schedule.getAvailableSlots().size());
    }

    @Test
    void shouldReturnAllSlots() {
        Schedule schedule = new Schedule();

        TimeSlot slot1 = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        TimeSlot slot2 = new TimeSlot(LocalDateTime.now().plusDays(2), 30);

        schedule.addSlot(slot1);
        schedule.addSlot(slot2);

        assertEquals(2, schedule.getAllSlots().size());
    }
}