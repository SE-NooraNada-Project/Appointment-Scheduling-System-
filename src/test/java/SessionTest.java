import com.appointment.domain.Administrator;
import com.appointment.domain.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void shouldLoginAdmin() {
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        session.login(admin);

        assertTrue(session.isAdminLoggedIn());
        assertEquals(admin, session.getLoggedInAdmin());
    }

    @Test
    void shouldLogoutAdmin() {
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        session.login(admin);
        session.logout();

        assertFalse(session.isAdminLoggedIn());
        assertNull(session.getLoggedInAdmin());
    }
}