package rxjava.examples.logic;

import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.examples.model.Email;
import rxjava.examples.model.EmailOrFailed;
import rxjava.examples.model.Ticket;

import java.text.MessageFormat;
import java.util.List;

public class EmailRxBL {

    private final EmailBL emailBL;


    public EmailRxBL() {
        emailBL = new EmailBL();
    }

    public List<EmailOrFailed> bulkSendEmailSequential(List<Ticket> tickets) {

        return Observable.from(tickets)
                .flatMap(ticket ->
                        trySendEmail(ticket)
                                .flatMap(response -> Observable.just(new EmailOrFailed().email(response)))
                                .doOnError(e -> System.err.println(MessageFormat.format("Failed to send {0} {1}", ticket, e)))
                                .onErrorReturn(err -> new EmailOrFailed().failed(ticket))
                ).toList().toBlocking().single();
    }

    public List<EmailOrFailed> bulkSendEmailParallel(List<Ticket> tickets){
        return Observable.from(tickets)
                .flatMap(ticket ->
                        trySendEmail(ticket)
                                .flatMap(response -> Observable.just(new EmailOrFailed().email(response)))
                                .doOnError(e -> System.err.println(MessageFormat.format("Failed to send {0} {1}", ticket, e)))
                                .onErrorReturn(err -> new EmailOrFailed().failed(ticket))
                                .subscribeOn(Schedulers.io())
                ).toList().toBlocking().single();
    }

    Observable<Email> trySendEmail(Ticket ticket) {
        return Observable.fromCallable(() -> emailBL
                .trySendEmail(ticket));
    }
}
