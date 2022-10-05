package rxjava.examples.errorhandling;

import rx.Observable;
import rxjava.examples.Log;
import rxjava.examples.model.Confirmation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Timeouts {
    public Observable<Confirmation> confirmation() {
        Observable<Confirmation> delayBeforeCompletion =
                Observable
                        .<Confirmation>empty()
                        .delay(200, TimeUnit.MILLISECONDS);
        return Observable.just(new Confirmation())
                .delay(100, TimeUnit.MILLISECONDS)
                .concatWith(delayBeforeCompletion);
    }

    public void timeoutExample(int timeoutDuration) {
        confirmation()

                .timeout(timeoutDuration, TimeUnit.MILLISECONDS)

                .forEach(Log::log, throwable -> {
                    if ((throwable instanceof TimeoutException)) {
                        Log.log("Too long");
                    } else {
                        throwable.printStackTrace();
                    }
                });
    }
}
