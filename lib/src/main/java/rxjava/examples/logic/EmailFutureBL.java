package rxjava.examples.logic;

import org.apache.commons.lang3.tuple.Pair;
import rxjava.examples.model.Email;
import rxjava.examples.model.Ticket;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailFutureBL {
    final EmailBL emailBL;
    final ExecutorService pool;

    public EmailFutureBL() {
        emailBL = new EmailBL();
        pool = ForkJoinPool.commonPool();
    }

    public Pair<List<Email>, List<Ticket>> bulkSendEmail(List<Ticket> tickets) {
        List<Pair<Ticket, Future<Email>>> tasks =
                tickets.stream().map(ticket -> Pair.of(ticket, getSendEmailFuture(ticket)))
                        .collect(Collectors.toList());

        List<Pair<Email, Ticket>> pairs = tasks.stream().flatMap(pair -> {
            try {
                Future<Email> future = pair.getRight();
                Email email = future.get(1, TimeUnit.SECONDS);
                return Stream.of(Pair.of(email, (Ticket) null));
            } catch (Exception ex) {
                Ticket ticket = pair.getLeft();
                System.err.println("Failed to send ticket " + ticket + " : " + ex);
                return Stream.of(Pair.of((Email) null, ticket));
            }
        }).collect(Collectors.toList());

        List<Email> emails = pairs.stream().filter(x -> x.getLeft() != null).map(Pair::getLeft).collect(Collectors.toList());
        List<Ticket> failures = pairs.stream().filter(x -> x.getRight() != null).map(Pair::getRight).collect(Collectors.toList());
        return Pair.of(emails, failures);
    }

    private Future<Email> getSendEmailFuture(Ticket ticket) {
        return pool.submit(() -> emailBL.trySendEmail(ticket));
    }
}
