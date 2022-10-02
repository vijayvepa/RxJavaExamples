package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rxjava.examples.samples.ExistingApplications;

import java.util.concurrent.TimeUnit;

import static rxjava.examples.Log.log;
import static rxjava.examples.samples.ExistingApplications.*;

public class ExistingApplicationsTests {

    @Test
    void immediateTest() {
        Scheduler scheduler = Schedulers.immediate();
        setupWorker(scheduler);
    }

    @Test
    void trampolineTest() {
        Scheduler scheduler = Schedulers.trampoline();
        setupWorker(scheduler);
    }

    @Test
    void simpleSubscribeTest() {


        Log.threadLog("Starting");
        Observable<String> obs = simple();
        Log.threadLog("Created");
        Observable<String> obs2 = obs.map(x -> x).filter(x -> true);
        Log.threadLog("Transformed");


        obs2.subscribe(x -> Log.threadLog("Got " + x),
                Throwable::printStackTrace,
                () -> Log.threadLog("Completed"));

    }

    @Test
    void simpleSubscribeOnSchedATest() {


        Log.threadLog("Starting");
        Observable<String> obs = simple();
        Log.threadLog("Created");
        Observable<String> obs2 = obs.map(x -> x).filter(x -> true);
        Log.threadLog("Transformed");


        obs2
                .subscribeOn(ExistingApplications.SCHEDULER_A)
                .subscribe(x -> Log.threadLog("Got " + x),
                        Throwable::printStackTrace,
                        () -> Log.threadLog("Completed"));

    }

    @Test
    void simpleSubscribeOnSchedA_and_BTest() {


        Log.threadLog("Starting");
        Observable<String> obs = simple();
        Log.threadLog("Created");
        Observable<String> obs2 = obs.map(x -> x).filter(x -> true);
        Log.threadLog("Transformed");


        obs2
                .subscribeOn(ExistingApplications.SCHEDULER_B)
                .subscribeOn(ExistingApplications.SCHEDULER_A)
                .subscribe(x -> Log.threadLog("Got " + x),
                        Throwable::printStackTrace,
                        () -> Log.threadLog("Completed"));

    }

    @Test
    void simpleMapDetailsTest() {


        Log.threadLog("Starting");
        Observable<String> obs = simple();
        Log.threadLog("Created");


        obs
                .doOnNext(Log::threadLog)
                .map(x -> x + '1')
                .doOnNext(Log::threadLog)
                .map(x -> x + '2')
                .subscribeOn(ExistingApplications.SCHEDULER_A)
                .subscribe(x -> Log.threadLog("Got " + x),
                        Throwable::printStackTrace,
                        () -> Log.threadLog("Completed"));

        Log.threadLog("Exiting");
    }

    @Test
    void observeOnTest() {
        log("Starting");
        final Observable<String> obs = simple();
        log("Created");

        obs.doOnNext(x -> log("Found 1:" + x))
                .observeOn(ExistingApplications.SCHEDULER_A)
                .doOnNext(x -> log("Found 2:" + x))
                .subscribe(
                        x -> log("Got 1:" + x),
                        Throwable::printStackTrace,
                        () -> log("Completed"));
    }

    @Test
    void observeOnTest2() {
        log("Starting");
        final Observable<String> obs = simple();
        log("Created");

        obs.doOnNext(x -> log("Found 1:" + x))
                .observeOn(ExistingApplications.SCHEDULER_B)
                .doOnNext(x -> log("Found 2:" + x))
                .observeOn(ExistingApplications.SCHEDULER_C)
                .doOnNext(x -> log("Found 3:" + x))
                .subscribeOn(SCHEDULER_A)
                .subscribe(
                        x -> log("Got 1:" + x),
                        Throwable::printStackTrace,
                        () -> log("Completed"));

        log("Exiting");
    }

    @Test
    void observeOnTest3() {
        log("Starting");

        final Observable<Object> obs = Observable.create(subscriber -> {
            log("Subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onNext("C");
            subscriber.onNext("D");
            subscriber.onCompleted();
        });


        log("Created");

        obs.
                subscribeOn(SCHEDULER_A)
                .flatMap(record -> ExistingApplications.store(record).subscribeOn(SCHEDULER_B))
                .observeOn(SCHEDULER_C)
                .toBlocking()
                .subscribe(
                        x -> log("Got:" + x),
                        Throwable::printStackTrace,
                        () -> log("Completed"));

        log("Exiting");
    }

    @Test
    void customDelayThreadTest() {
        Observable.just("A", "B")
                .delay(1, TimeUnit.SECONDS, SCHEDULER_A)
                .doOnNext(x -> log("Delay for " + x))
                .toBlocking()
                .subscribe(Log::log);

    }

    @Test
    void defaultDelayThreadTest() {
        Observable.just("A", "B")
                .delay(1, TimeUnit.SECONDS)
                .doOnNext(x -> log("Delay for " + x))
                .toBlocking()
                .subscribe(Log::log);

    }


}
