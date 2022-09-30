package springexample;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import rx.Observable;
import rx.observables.ConnectableObservable;
import twitter4j.Status;

@Configuration
public class Config implements ApplicationListener<ContextRefreshedEvent> {

    private final Log log;
    private final ConnectableObservable<Status> observable;

    public Config(Log log) {
        this.log = log;
        observable = Observable.<Status>create(subscriber -> log.info("Starting...")).publish();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Connecting");
        observable.connect();
    }

    @Bean
    public Observable<Status> observable(){
        return observable;
    }


}
