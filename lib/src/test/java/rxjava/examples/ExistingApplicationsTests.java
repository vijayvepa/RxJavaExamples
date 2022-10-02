package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rxjava.examples.samples.ExistingApplications;

import static rxjava.examples.samples.ExistingApplications.setupWorker;
import static rxjava.examples.samples.ExistingApplications.simple;

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


}
