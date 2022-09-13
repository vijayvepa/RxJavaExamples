package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.logic.ReservationRxBL;
import rxjava.examples.model.Email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReservationRxBLTest {
    private final ReservationRxBL reservationBL = new ReservationRxBL();

    @Test
    public void bookTicketSequential() {

        Email email = TimeIt.printTime(() -> ObservableUtils.toObject(reservationBL.reserveTicketSequential("CK238", "45")));
        assertEquals("CK238", email.ticket().flight().flightNumber());
        assertNotNull(email.ticket().date());

        assertNotNull(email.emailDate());
    }

    @Test
    public void bookTicketParallel() {

        Email email = TimeIt.printTime(() -> ObservableUtils.toObject(reservationBL.reserveTicketParallel("CK238", "45")));
        assertEquals("CK238", email.ticket().flight().flightNumber());
        assertNotNull(email.ticket().date());

        assertNotNull(email.emailDate());
    }

    @Test
    public void bookTicketSquared() {

        Email email = TimeIt.printTime(() -> ObservableUtils.toObject(reservationBL.reserveTicketSquared("CK238", "45")));
        assertEquals("CK238", email.ticket().flight().flightNumber());
        assertNotNull(email.ticket().date());

        assertNotNull(email.emailDate());
    }
}
