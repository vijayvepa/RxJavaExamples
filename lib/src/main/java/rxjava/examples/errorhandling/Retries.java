package rxjava.examples.errorhandling;

import rx.Observable;
import rxjava.examples.Log;

import java.util.concurrent.TimeUnit;

public class Retries {

    public Observable<String> risky() {
        return Observable.fromCallable(() -> {
            if (Math.random() < 0.1) {
                Thread.sleep((long) (Math.random() * 2000));
                return "OK";
            } else {
                throw new RuntimeException("Transient");
            }
        });
    }

    public void retryExample() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))
                .retry()
                .toBlocking()
                .subscribe(Log::log);
    }
}
