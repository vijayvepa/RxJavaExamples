package rxjava.examples;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import rxjava.examples.logic.EmailBL;
import rxjava.examples.model.Email;
import rxjava.examples.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EmailBLTest {

    private final EmailBL emailBL = new EmailBL();

    @Test
    public void bulkSendEmail() {
        List<Ticket> tickets = IntStream.range(0, 10).mapToObj(x -> new Ticket()).collect(Collectors.toList());
        Pair<List<Email>, List<Ticket>> bulkSendEmail = emailBL.bulkSendEmail(tickets);

        assertNotEquals(0, bulkSendEmail.getLeft().size());
        assertNotEquals(0, bulkSendEmail.getRight().size());
    }
}
