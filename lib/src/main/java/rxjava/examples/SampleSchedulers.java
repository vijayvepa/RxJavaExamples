package rxjava.examples;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;
public class SampleSchedulers {




    ExecutorService poolA = newFixedThreadPool(10, threadFactory("Sched-A-%d"));
    public Scheduler schedulerA = Schedulers.from(poolA);

    ExecutorService poolB = newFixedThreadPool(10, threadFactory("Sched-B-%d"));
    public Scheduler schedulerB = Schedulers.from(poolB);

    ExecutorService poolC = newFixedThreadPool(10, threadFactory("Sched-C-%d"));
    public Scheduler schedulerC = Schedulers.from(poolC);

    private ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .build();
    }
}
