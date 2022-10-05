package rxjava.examples.errorhandling;

import rx.Observable;
import rxjava.examples.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public void retryWithLimitExample() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))
                .retry(3)
                .toBlocking()
                .subscribe(Log::log, Throwable::printStackTrace);
    }

    public void retryWithLimitAltExample() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))
                .retryWhen(failures -> failures.take(3))
                .toBlocking()
                .subscribe(Log::log, Throwable::printStackTrace);
    }

    public void retryWithDropExample() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))
                .retry((attempt, e) ->
                        attempt <= 10 && !(e instanceof TimeoutException)
                )
                .toBlocking()
                .subscribe(Log::log, Throwable::printStackTrace);
    }

    public void retryWithSimpleBackoffDelay() {
        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))
                .retryWhen(failures -> failures.delay(1, TimeUnit.SECONDS))
                .toBlocking()
                .subscribe(Log::log, Throwable::printStackTrace);
    }

    public void retryWithAttempts(int attempts) {

        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))

                .retryWhen(failures -> failures
                        .zipWith(Observable.range(1, attempts), (err, attempt) ->
                                attempt < attempts ?
                                        Observable.timer(1, TimeUnit.SECONDS) :
                                        Observable.error(err)

                        )
                )


                .toBlocking()
                .subscribe(Log::log, Throwable::printStackTrace);
    }

    public void retryWithExponentialBackoff(int attempts) {

        risky()
                .timeout(1, TimeUnit.SECONDS)
                .doOnError(throwable -> Log.log("WARN: Will retry: " + throwable))

                .retryWhen(failures -> failures
                        .zipWith(Observable.range(1, attempts), (err, attempt) -> handleRetryAttempt(err, attempt, attempts))
                        .flatMap(x -> x)
                )


                .toBlocking()
                .subscribe(Log::log, Throwable::printStackTrace);
    }

    private Observable<Long> handleRetryAttempt(Throwable err, Integer attempt, Integer maxAttempts) {
        if (attempt == 1) {
            return Observable.just(42L);
        } else if (attempt.equals(maxAttempts)) {
            return Observable.error(err);
        } else {
            long expDelay = (long) Math.pow(2, attempt - 2);
            Log.log("Attempt" + attempt + ",Delaying " + expDelay);
            return Observable.timer(expDelay, TimeUnit.SECONDS);
        }
    }
}
