package rxjava.examples.errorhandling;

import rx.Observable;
import rxjava.examples.Log;
import rxjava.examples.model.Confirmation;

import java.time.LocalDate;
import java.time.Month;
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

    public Observable<LocalDate> nextSolarEclipse(LocalDate after) {
        return Observable
                .just(
                        LocalDate.of(2016, Month.MARCH, 9),
                        LocalDate.of(2016, Month.SEPTEMBER, 1),
                        LocalDate.of(2017, Month.FEBRUARY, 26),
                        LocalDate.of(2017, Month.AUGUST, 21),
                        LocalDate.of(2018, Month.FEBRUARY, 15),
                        LocalDate.of(2018, Month.JULY, 13),
                        LocalDate.of(2018, Month.AUGUST, 11),
                        LocalDate.of(2019, Month.JANUARY, 6),
                        LocalDate.of(2019, Month.JULY, 2),
                        LocalDate.of(2019, Month.DECEMBER, 26)
                )
                .skipWhile(localDate -> !localDate.isAfter(after))
                .zipWith(Observable.interval(500, 50, TimeUnit.MILLISECONDS),
                        (localDate, aLong) -> localDate);
    }

    public void timeoutExampleWithHandshake() {
        nextSolarEclipse(LocalDate.of(2016, Month.SEPTEMBER, 1))
                .timeout(
                        () -> Observable.timer(1000, TimeUnit.MILLISECONDS),
                        date -> Observable.timer(100, TimeUnit.MILLISECONDS)
                )
                .forEach(Log::log, throwable -> {
                    if ((throwable instanceof TimeoutException)) {
                        Log.log("Too long");
                    } else {
                        throwable.printStackTrace();
                    }
                });
        ;
    }

    public void timeoutExampleWithFallback() {
        nextSolarEclipse(LocalDate.of(2016, Month.SEPTEMBER, 1))
                .timeout(
                        100, TimeUnit.MILLISECONDS, Observable.just(LocalDate.of(2000, Month.MARCH, 1))
                )
                .toBlocking().subscribe(Log::log);
    }

    public void trackLatency() {
        nextSolarEclipse(LocalDate.of(2016, Month.JANUARY, 1)).timeInterval()
                .toBlocking().subscribe(Log::log);
    }

}
