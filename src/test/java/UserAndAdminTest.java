import com.appointment.domain.Administrator;
import com.appointment.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserAndAdminTest {

    @Test
    void shouldReturnUserFieldsCorrectly() {
        User user = new User("1", "Ali", "ali@test.com");

        assertEquals("1", user.getId());
        assertEquals("Ali", user.getName());
        assertEquals("ali@test.com", user.getEmail());
    }

    @Test
    void shouldReturnAdministratorUsername() {
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        assertEquals("admin", admin.getUsername());
        assertEquals("Admin", admin.getName());
        assertEquals("admin@test.com", admin.getEmail());
    }

    @Test
    void shouldValidateAdministratorPasswordCorrectly() {
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        assertTrue(admin.isPasswordCorrect("1234"));
        assertFalse(admin.isPasswordCorrect("wrong"));
    }
}