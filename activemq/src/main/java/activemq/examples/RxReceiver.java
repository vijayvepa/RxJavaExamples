package activemq.examples;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.subjects.PublishSubject;

@Component
public class RxReceiver {

    private final PublishSubject<Email> emailPublishSubject;

    public RxReceiver() {
        emailPublishSubject =  PublishSubject.create();
    }

    Observable<Email> observe(){
        return emailPublishSubject;
    }

    @JmsListener(destination = "rxmailbox",containerFactory = "myFactory")
    public void receiveMessage(Email email){

        emailPublishSubject.onNext(email);
    }
}
