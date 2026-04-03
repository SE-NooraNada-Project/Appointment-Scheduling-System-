import com.appointment.domain.Appointment;
import com.appointment.domain.AppointmentType;
import com.appointment.domain.TimeSlot;
import com.appointment.domain.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.appointment.repository.InMemoryAppointmentRepository;

public class InMemoryAppointmentRepositoryTest {

    @Test
    void shouldNotSaveNullAppointment() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        repository.save(null);

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingAppointment() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = new Appointment(user, slot, 1, AppointmentType.INDIVIDUAL);

        boolean result = repository.delete(appointment);

        assertFalse(result);
    }

    @Test
    void shouldReturnEmptyListWhenUserIdNotFound() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = new Appointment(user, slot, 1, AppointmentType.INDIVIDUAL);
        repository.save(appointment);

        List<Appointment> result = repository.findByUserId("999");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnCopyFromFindAll() {
        InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();

        User user = new User("1", "Ali");
        TimeSlot slot = new TimeSlot(LocalDateTime.now().plusDays(1), 30);
        Appointment appointment = new Appointment(user, slot, 1, AppointmentType.INDIVIDUAL);
        repository.save(appointment);

        List<Appointment> result = repository.findAll();
        result.clear();

        assertEquals(1, repository.findAll().size());
    }
}