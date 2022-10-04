package rxjava.examples.flowcontrol;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Timestamped;
import rxjava.examples.model.TeleData;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer {
    private final Random random;

    public Producer() {
        random = new Random();
    }

    public Observable<String> names() {
        return Observable.just(
                "Mary", "Patricia", "Linda",
                "Barbara",

                "Elizabeth", "Jennifer", "Maria", "Susan",
                "Margaret", "Dorothy");
    }

    public Observable<Long> delayMillis() {
        return Observable.just(
                        0.1, 0.6, 0.9,
                        1.1,

                        3.3, 3.4, 3.5, 3.6,
                        4.4, 4.8)
                .map(d -> (long) (d * 1_000));
    }

    public Observable<BigDecimal> prices() {
        return Observable.just(
                125, 130, 140,
                125,

                130, 140, 150, 155,
                160, 165).map(BigDecimal::new);
    }

    public Observable<Timestamped<Long>> observableAt10msFrequency() {
        return Observable.interval(10, TimeUnit.MILLISECONDS).timestamp();
    }

    public Observable<Long> intervalObservable(int millis) {
        return Observable.interval(millis, TimeUnit.MILLISECONDS);
    }

    public ConnectableObservable<Long> connectableInterval(int millis) {
        return Observable.interval(millis, TimeUnit.MILLISECONDS).publish();
    }

    public Observable<Integer> ranged() {
        return Observable.range(1, 7);
    }

    public Observable<Double> randomGaussian() {
        return Observable.defer(() -> Observable.just(random.nextGaussian())).repeat(1000);
    }

    public Observable<TeleData> teleData() {
        return Observable.interval(10, TimeUnit.MILLISECONDS).map(x -> TeleData.builder().timestamp(x).value(random.nextGaussian()).build());
    }
}