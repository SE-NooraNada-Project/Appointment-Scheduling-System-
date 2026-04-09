import com.appointment.domain.Administrator;
import com.appointment.domain.Session;
import com.appointment.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void loginShouldSucceedWithCorrectCredentials() {

        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");
        Session session = new Session();
        AuthService auth = new AuthService();

        boolean result = auth.login(session, admin, "admin", "1234");

        assertTrue(result);
        assertTrue(session.isAdminLoggedIn());
    }

    @Test
    void loginShouldFailWithWrongPassword() {

        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");
        Session session = new Session();
        AuthService auth = new AuthService();

        boolean result = auth.login(session, admin, "admin", "wrong");

        assertFalse(result);
    }

    @Test
    void shouldLoginSuccessfully() {
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        AuthService service = new AuthService();

        boolean result = service.login(session, admin, "admin", "1234");

        assertTrue(result);
        assertTrue(session.isAdminLoggedIn());
    }

    @Test
    void shouldFailLoginWithWrongUsername() {
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        AuthService service = new AuthService();

        boolean result = service.login(session, admin, "wrong", "1234");

        assertFalse(result);
    }

    @Test
    void shouldLogout() {
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        session.login(admin);

        AuthService service = new AuthService();
        service.logout(session);

        assertFalse(session.isAdminLoggedIn());
    }

    @Test
    void shouldFailLoginWhenSessionIsNull() {
        AuthService auth = new AuthService();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        boolean result = auth.login(null, admin, "admin", "1234");

        assertFalse(result);
    }

    @Test
    void shouldFailLoginWhenAdminIsNull() {
        AuthService auth = new AuthService();
        Session session = new Session();

        boolean result = auth.login(session, null, "admin", "1234");

        assertFalse(result);
    }

    @Test
    void shouldFailLoginWhenUsernameIsNull() {
        AuthService auth = new AuthService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        boolean result = auth.login(session, admin, null, "1234");

        assertFalse(result);
    }

    @Test
    void shouldFailLoginWhenPasswordIsNull() {
        AuthService auth = new AuthService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        boolean result = auth.login(session, admin, "admin", null);

        assertFalse(result);
    }

    @Test
    void shouldFailLoginWithWrongCredentials() {
        AuthService auth = new AuthService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        boolean result = auth.login(session, admin, "wrong", "wrong");

        assertFalse(result);
        assertFalse(session.isAdminLoggedIn());
    }

    @Test
    void shouldLogoutSuccessfully() {
        AuthService auth = new AuthService();
        Session session = new Session();
        Administrator admin = new Administrator("1", "Admin", "admin@test.com", "admin", "1234");

        auth.login(session, admin, "admin", "1234");
        auth.logout(session);

        assertFalse(session.isAdminLoggedIn());
    }

    @Test
    void shouldNotFailWhenLogoutSessionIsNull() {
        AuthService auth = new AuthService();

        assertDoesNotThrow(() -> auth.logout(null));
    }
}