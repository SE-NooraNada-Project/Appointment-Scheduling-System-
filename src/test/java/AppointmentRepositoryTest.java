import com.appointment.domain.Appointment;
import com.appointment.domain.AppointmentType;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import com.appointment.repository.InMemoryAppointmentRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentRepositoryTest {

    @Test
    void shouldSaveAppointment() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        User user = new User("1", "Ali", "ali@test.com");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = new Appointment(user, slot, 1, AppointmentType.INDIVIDUAL);

        repository.save(appointment);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldFindAppointmentsByUserId() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        User user1 = new User("1", "Ali", "ali@test.com");
        User user2 = new User("2", "Sara", "sara@test.com");

        Appointment appointment1 = new Appointment(
                user1,
                new TimeSlot(LocalDateTime.now().plusDays(1), 30),
                1,
                AppointmentType.INDIVIDUAL
        );

        Appointment appointment2 = new Appointment(
                user2,
                new TimeSlot(LocalDateTime.now().plusDays(2), 30),
                1,
                AppointmentType.INDIVIDUAL
        );

        repository.save(appointment1);
        repository.save(appointment2);

        assertEquals(1, repository.findByUserId("1").size());
        assertEquals("Ali", repository.findByUserId("1").get(0).getUser().getName());
    }

    @Test
    void shouldDeleteAppointment() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        User user = new User("1", "Ali", "ali@test.com");
        Appointment appointment = new Appointment(
                user,
                new TimeSlot(LocalDateTime.now().plusDays(1), 30),
                1,
                AppointmentType.INDIVIDUAL
        );

        repository.save(appointment);

        boolean deleted = repository.delete(appointment);

        assertTrue(deleted);
        assertEquals(0, repository.findAll().size());
    }
}