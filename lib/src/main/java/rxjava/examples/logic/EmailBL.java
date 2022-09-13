package rxjava.examples.logic;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import rxjava.examples.model.Email;
import rxjava.examples.model.EmailNotFoundException;
import rxjava.examples.model.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmailBL {

    final Random random;

    public EmailBL() {
        random = new Random();
    }

    @SneakyThrows
    Email sendEmail(Ticket ticket) {

        System.out.println("Sending email...");
        Thread.sleep(500);

        return new Email().ticket(ticket).emailDate(LocalDateTime.now().toString());
    }

    @SneakyThrows
    Email trySendEmail(Ticket ticket) {

        System.out.println("Sending email...");
        Thread.sleep(500);

        if(random.nextInt()%5 == 0){
            throw new EmailNotFoundException();
        }

        return new Email().ticket(ticket).emailDate(LocalDateTime.now().toString());
    }

    public Pair<List<Email>, List<Ticket>> bulkSendEmail(List<Ticket> tickets) {
        List<Ticket> failures = new ArrayList<>();
        List<Email> sent = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                Email email = trySendEmail(ticket);
                sent.add(email);
            } catch (Exception ex) {
                System.err.println("Failed to send " + ticket + " :" + ex);
                failures.add(ticket);
            }
        }
        return Pair.of(sent, failures);
    }

}
