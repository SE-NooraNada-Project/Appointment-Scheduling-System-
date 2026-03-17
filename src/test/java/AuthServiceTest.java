import com.appointment.domain.Administrator;
import com.appointment.domain.Session;
import com.appointment.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void loginShouldSucceedWithCorrectCredentials() {

        Administrator admin = new Administrator("1","Admin","admin","1234");
        Session session = new Session();
        AuthService auth = new AuthService();

        boolean result = auth.login(session, admin, "admin", "1234");

        assertTrue(result);
        assertTrue(session.isAdminLoggedIn());
    }

    @Test
    void loginShouldFailWithWrongPassword() {

        Administrator admin = new Administrator("1","Admin","admin","1234");
        Session session = new Session();
        AuthService auth = new AuthService();

        boolean result = auth.login(session, admin, "admin", "wrong");

        assertFalse(result);
    }
}