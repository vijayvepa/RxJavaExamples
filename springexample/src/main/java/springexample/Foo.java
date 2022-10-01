package springexample;

import org.springframework.stereotype.Component;
import rx.Observable;
import twitter4j.Status;

@Component
public class Foo {

    private final Log log;

    public Foo(Observable<Status> tweets, Log log) {
        this.log = log;
        tweets.subscribe(status -> log.info(status.getText()));
        log.info("Subscribed");
    }
}
