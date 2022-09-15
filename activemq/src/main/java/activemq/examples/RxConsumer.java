package activemq.examples;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class RxConsumer implements InitializingBean {

    private final RxReceiver rxReceiver;

    public RxConsumer(RxReceiver rxReceiver) {
        this.rxReceiver = rxReceiver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rxReceiver.observe().subscribe(email ->
                System.out.println("Observed " + email)
        );
    }
}
