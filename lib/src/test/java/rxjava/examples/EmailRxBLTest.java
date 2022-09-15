package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.logic.EmailRxBL;
import rxjava.examples.model.EmailOrFailed;
import rxjava.examples.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EmailRxBLTest {

    private final EmailRxBL emailBL = new EmailRxBL();

    @Test
    public void bulkSendEmailSequential() {
        List<Ticket> tickets = IntStream.range(0, 10).mapToObj(x -> new Ticket()).collect(Collectors.toList());
        List<EmailOrFailed> bulkSendEmail = emailBL.bulkSendEmailSequential(tickets);

        assertNotEquals(0, bulkSendEmail.stream().filter(x -> x.failed() != null).count());
        assertNotEquals(0, bulkSendEmail.stream().filter(x -> x.email() != null).count());
    }

    @Test
    public void bulkSendEmailParallel() {
        List<Ticket> tickets = IntStream.range(0, 10).mapToObj(x -> new Ticket()).collect(Collectors.toList());
        List<EmailOrFailed> bulkSendEmail = emailBL.bulkSendEmailParallel(tickets);

        assertNotEquals(0, bulkSendEmail.stream().filter(x -> x.failed() != null).count());
        assertNotEquals(0, bulkSendEmail.stream().filter(x -> x.email() != null).count());
    }

}
