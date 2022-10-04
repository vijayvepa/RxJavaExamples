package rxjava.examples.flowcontrol;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Timestamped;
import rxjava.examples.Log;
import rxjava.examples.model.Book;
import rxjava.examples.model.TeleData;
import rxjava.examples.utils.ObservableUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Consumer {


    public void samplingTimeStampedObservable(Observable<Timestamped<Long>> timestampedObservable) {
        long startTime = System.currentTimeMillis();

        timestampedObservable.sample(1, TimeUnit.SECONDS)
                .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
                .take(5).toBlocking().subscribe(Log::log);
    }

    public void samplingStringObservable(Observable<String> observable) {
        observable.sample(1, TimeUnit.SECONDS).toBlocking().subscribe(Log::log);
    }

    public Observable<Long> delayMillis() {
        return Observable.just(
                        0.25, 0.5,
                        1.0,
                        1.5,
                        3.0)
                .map(d -> (long) (d * 1_000));
    }


    public void samplingDynamicStringObservable(Observable<String> observable) {


        final Observable<Long> interval =
                ObservableUtils.delayedEmit(Observable.just(1L, 2L, 3L, 4L), delayMillis());


        observable.sample(interval).toBlocking().subscribe(Log::log);
    }

    public void throttleFirstStringObservable(Observable<String> observable) {
        observable.throttleFirst(1, TimeUnit.SECONDS).toBlocking().subscribe(Log::log);
    }

    public void throttleLastStringObservable(Observable<String> observable) {
        observable.throttleFirst(1, TimeUnit.SECONDS).toBlocking().subscribe(Log::log);
    }

    public void buffered(Observable<Integer> observable) {
        observable.buffer(3).subscribe(Log::log);
    }

    public void bufferedWithOverlap(Observable<Integer> observable) {
        observable.buffer(3, 1).subscribe(Log::log);
    }

    public void bufferedWithSkip(Observable<Integer> observable) {
        observable.buffer(1, 2).subscribe(Log::log);
    }

    public void bufferedWithSkipAndFlat(Observable<Integer> observable) {
        observable.buffer(1, 2).flatMapIterable(x -> x).subscribe(Log::log);
    }

    public void storeBuffered(Repository repository) {
        Observable<Book> books = Observable.just(new Book()).repeat();
        books.buffer(10).subscribe(repository::storeAll);

    }

    public void movingAverage(Observable<Double> observable) {
        observable.buffer(100, 1).map(this::averageOfList).subscribe(Log::log);
    }

    private double averageOfList(List<Double> doubles) {
        return doubles.stream().collect(Collectors.averagingDouble(x -> x));
    }

    public void bufferWithTimePeriod(Observable<String> observable) {
        observable.buffer(1, TimeUnit.SECONDS).toBlocking().subscribe(System.out::println);
    }


    public void windowBuffering(Observable<TeleData> observable) {
        final Observable<Observable<TeleData>> window = observable.window(1, TimeUnit.SECONDS);
        window.flatMap(Observable::count).toBlocking().subscribe(Log::log);
    }

    public <T> void debounce(Observable<T> observable) {
        observable.debounce(500, TimeUnit.MILLISECONDS).toBlocking().subscribe(Log::log);
    }

    public void conditionalDebounce(Observable<BigDecimal> observable) {
        observable.debounce(x -> {
            boolean goodPrice = x.compareTo(BigDecimal.valueOf(150)) > 0;
            return Observable.empty().delay(goodPrice ? 10 : 100, TimeUnit.MILLISECONDS);
        }).toBlocking().subscribe(Log::log);
    }

    public void starvingDebounce(int debounceMs, Observable<Long> fasterObservable) {
        fasterObservable.debounce(debounceMs, TimeUnit.MILLISECONDS).toBlocking().subscribe(Log::log);
    }

    public void starvingDebounceWithTimeout(int debounceMs, Observable<Long> fasterObservable) {
        fasterObservable.debounce(debounceMs, TimeUnit.MILLISECONDS)
                .timeout(1, TimeUnit.SECONDS)
                .toBlocking().subscribe(Log::log, Throwable::printStackTrace);
    }

    public void starvingDebounceWithRecoveryBad1(int debounceMs, ConnectableObservable<Long> fasterObservable) {
        fasterObservable.debounce(debounceMs, TimeUnit.MILLISECONDS)
                .timeout(1, TimeUnit.SECONDS, fasterObservable.take(1))
                .toBlocking().subscribe(Log::log, Throwable::printStackTrace);
        ;


        fasterObservable.connect();
    }

    public void starvingDebounceWithRecoveryBad2(int debounceMs, ConnectableObservable<Long> fasterObservable) {
        fasterObservable.debounce(debounceMs, TimeUnit.MILLISECONDS)
                .timeout(1, TimeUnit.SECONDS,
                        fasterObservable.take(1).concatWith(fasterObservable.debounce(debounceMs, TimeUnit.MILLISECONDS))
                )

                .toBlocking().subscribe(Log::log, Throwable::printStackTrace);
        ;


        fasterObservable.connect();
    }

    public Observable<Long> timedDebounce(int debounceMs, Observable<Long> fastObservable) {
        Observable<Long> onTimeout = fastObservable.take(1).concatWith(Observable.defer(() -> timedDebounce(debounceMs, fastObservable)));

        return fastObservable.debounce(debounceMs, TimeUnit.MILLISECONDS).timeout(1, TimeUnit.SECONDS, onTimeout);

    }
}
