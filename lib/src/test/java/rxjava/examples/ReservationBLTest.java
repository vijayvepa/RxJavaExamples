package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.logic.ReservationBL;
import rxjava.examples.model.Email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReservationBLTest {
    private final ReservationBL reservationBL = new ReservationBL();

    @Test
    public void bookTicket(){

        Email email = TimeIt.printTime(()->reservationBL.reserveTicket("CK238", "45"));
        assertEquals("CK238", email.ticket().flight().flightNumber());
        assertNotNull(email.ticket().date());

        assertNotNull(email.emailDate());
    }
}
