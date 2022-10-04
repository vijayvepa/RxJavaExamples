package rxjava.examples.flowcontrol;

import rx.Observable;
import rx.schedulers.Timestamped;
import rxjava.examples.Log;
import rxjava.examples.model.Book;
import rxjava.examples.model.TeleData;
import rxjava.examples.utils.ObservableUtils;

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
}
