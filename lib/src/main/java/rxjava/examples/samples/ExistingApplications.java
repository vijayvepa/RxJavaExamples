package rxjava.examples.samples;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rxjava.examples.Log;

import java.util.concurrent.Executors;

public class ExistingApplications {

    public static final Scheduler SCHEDULER_A = namedScheduler("Sched-A-%d");
    public static final Scheduler SCHEDULER_B = namedScheduler("Sched-B-%d");
    public static final Scheduler SCHEDULER_C = namedScheduler("Sched-C-%d");


    public static Scheduler namedScheduler(String pattern) {
        return Schedulers.from(
                Executors.newFixedThreadPool(10,
                        new ThreadFactoryBuilder().setNameFormat(pattern).build()));
    }


    public static Observable<String> simple() {
        return Observable.create(s -> {
                    Log.threadLog("Subscribed");
                    s.onNext("A");
                    s.onNext("B");
                    s.onCompleted();
                }
        );
    }

    public static void setupWorker(Scheduler scheduler) {
        Scheduler.Worker worker = scheduler.createWorker();

        Log.threadLog("Main start");
        worker.schedule(() -> {
            Log.threadLog(" Outer start");
            sleepOneSecond();

            worker.schedule(() -> {
                Log.threadLog(" Middle start");
                sleepOneSecond();
                worker.schedule(() -> {
                    Log.threadLog(" Inner start");
                    sleepOneSecond();
                    Log.threadLog(" Inner end");
                });

                Log.threadLog(" Middle end");
            });

            Log.threadLog(" Outer end");
        });
        Log.threadLog("Main end");
    }

    public static void sleepOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }
    }
}
