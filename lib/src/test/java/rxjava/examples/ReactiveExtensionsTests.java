package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;
import rxjava.examples.samples.ReactiveExtensions;

import java.util.concurrent.TimeUnit;

import static rxjava.examples.Log.log;

public class ReactiveExtensionsTests {

    private final ReactiveExtensions reactiveExtensions = new ReactiveExtensions();

    @Test
    public void rangeTest() {
        log("Before");
        Observable.range(5, 3).subscribe(Log::log);
        log("After");
    }

    @Test
    public void lowLevelRangeTest() {
        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(5);
                        subscriber.onNext(6);
                        subscriber.onNext(7);
                        subscriber.onCompleted();
                        log("Complete");
                    }
                });

        log("Starting");
        ints.subscribe(i -> log("Element" + i));
        log("Exit");
    }


    @Test
    public void multipleSubscribersTest() {
        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(32);
                        log("Complete");
                    }
                });

        log("Starting");
        ints.subscribe(i -> log("Element A:" + i));
        ints.subscribe(i -> log("Element B:" + i));
        log("Exit");
    }

    @Test
    public void multipleSubscribersWithCacheTest() {
        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(32);
                        log("Complete");
                    }
                }).cache();

        log("Starting");
        ints.subscribe(i -> log("Element A:" + i));
        ints.subscribe(i -> log("Element B:" + i));
        log("Exit");
    }

    @Test
    public void delayedTest1() {
        Observable<Integer> delayed = reactiveExtensions.delayed(10);

        delayed.subscribe(System.out::println);

        long startTime = System.currentTimeMillis();
        delayed.subscribe(s -> {
                    while (System.currentTimeMillis() - startTime < 1000) {

                        log("Waiting for 100 ms");
                        reactiveExtensions.sleep(100, TimeUnit.MILLISECONDS);
                    }
                    log("Received" + s);
                }
        );

        reactiveExtensions.sleep(11, TimeUnit.SECONDS);
    }

    @Test
    public void delayedTest2() {
        Observable<Integer> delayed = reactiveExtensions.delayed(10);

        delayed.subscribe(System.out::println);

        long startTime = System.currentTimeMillis();
        delayed.subscribe(s -> {
                    while (System.currentTimeMillis() - startTime < 1000) {

                        log("Waiting for 100 ms");
                        reactiveExtensions.sleep(100, TimeUnit.MILLISECONDS);
                    }
                    log("Received" + s);
                }
        );

        reactiveExtensions.sleep(1, TimeUnit.SECONDS);
    }

    @Test
    void timedObservableTest() {
        reactiveExtensions.timedObservable().subscribe(Log::log);
        reactiveExtensions.sleep(2, TimeUnit.SECONDS);
    }

    @Test
    void intervalObservableTest() {
        reactiveExtensions.intervalBasedObservable().subscribe(Log::log);
        reactiveExtensions.sleep(2, TimeUnit.SECONDS);

    }

    @Test
    void scheduleAtFixedRateTest() {
        reactiveExtensions.scheduleAtFixedRate(() -> log("."));
        reactiveExtensions.sleep(2, TimeUnit.SECONDS);

    }


}
