import com.appointment.presentation.Main;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainCoverageTest {

    @Test
    void shouldCoverMainMenuBranches() {
        String input =
                "abc\n" +        // invalid number for main menu
                        "99\n" +         // invalid menu choice

                        "1\n" +          // admin login
                        "admin\n" +
                        "wrong\n" +      // wrong password

                        "1\n" +          // admin login correct
                        "admin\n" +
                        "1234\n" +

                        "1\n" +          // login again while already logged in

                        "5\n" +          // view my appointments -> none
                        "U100\n" +

                        "6\n" +          // modify my appointment -> none
                        "U100\n" +

                        "7\n" +          // cancel my appointment -> none
                        "U100\n" +

                        "8\n" +          // admin manage reservations
                        "9\n" +          // invalid submenu choice
                        "1\n" +          // view all reservations -> none
                        "2\n" +          // modify reservation -> none
                        "3\n" +          // cancel reservation -> none
                        "0\n" +          // back

                        "4\n" +          // book appointment
                        "U1\n" +
                        "Ali\n" +
                        "ali@test.com\n" +
                        "99\n" +         // invalid slot number

                        "4\n" +          // book appointment again
                        "U1\n" +
                        "Ali\n" +
                        "ali@test.com\n" +
                        "1\n" +          // valid slot
                        "abc\n" +        // invalid duration input
                        "30\n" +
                        "xyz\n" +        // invalid participants input
                        "1\n" +
                        "99\n" +         // invalid appointment type

                        "4\n" +          // valid booking
                        "U1\n" +
                        "Ali\n" +
                        "ali@test.com\n" +
                        "1\n" +
                        "30\n" +
                        "1\n" +
                        "6\n" +          // INDIVIDUAL

                        "5\n" +          // view appointments after booking
                        "U1\n" +

                        "9\n" +          // send mock reminders

                        "6\n" +          // modify my appointment
                        "U1\n" +
                        "1\n" +          // choose first appointment
                        "1\n" +          // choose new slot

                        "7\n" +          // cancel my appointment
                        "U1\n" +
                        "1\n" +          // choose first appointment

                        "10\n" +         // send email reminders when no appointments

                        "2\n" +          // logout
                        "2\n" +          // logout again while no admin logged in

                        "8\n" +          // admin manage without login

                        "0\n";           // exit

        runMainWithInput(input);
    }



    private void runMainWithInput(String data) {
        InputStream originalIn = System.in;
        try {
            ByteArrayInputStream testIn =
                    new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            System.setIn(testIn);
            assertDoesNotThrow(() -> Main.main(new String[]{}));
        } finally {
            System.setIn(originalIn);
        }
    }
}