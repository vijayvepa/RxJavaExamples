package rxjava.examples.samples;

import rx.Observable;
import rx.schedulers.Timestamped;
import rxjava.examples.Log;

import java.util.concurrent.TimeUnit;

public class FlowControl {

    public Observable<Timestamped<Long>> observableAt10msFrequency() {
        return Observable.interval(10, TimeUnit.MILLISECONDS).timestamp();
    }

    public void sampleAtOneSecond() {
        long startTime = System.currentTimeMillis();
        final Observable<Timestamped<Long>> timestampedObservable = observableAt10msFrequency();

        timestampedObservable.sample(1, TimeUnit.SECONDS)
                .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
                .take(5).toBlocking().subscribe(Log::log);
    }

    public Observable<String> names() {
        return Observable.just("Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Margaret", "Dorothy");
    }

    public Observable<Long> delayMillis() {
        return Observable.just(0.1, 0.6, 0.9, 1.1, 3.3, 3.4, 3.5, 3.6, 4.4, 4.8).map(d -> (long) (d * 1_000));
    }

    public Observable<String> delayedNames() {
        return names().zipWith(
                delayMillis(), (n, d) ->
                        Observable.just(n + "(" + d + ")")
                                .delay(d, TimeUnit.MILLISECONDS)
        ).flatMap(o -> o);
    }


    public void sampleAtOneSecond(Observable<String> observable) {
        observable.sample(1, TimeUnit.SECONDS).toBlocking().subscribe(Log::log);
    }
}
