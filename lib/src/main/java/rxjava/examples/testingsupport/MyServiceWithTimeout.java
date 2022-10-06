package rxjava.examples.testingsupport;

import rx.Observable;
import rx.Scheduler;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class MyServiceWithTimeout implements MyService {

    private final MyService delegate;
    private final Scheduler scheduler;

    public MyServiceWithTimeout(MyService delegate, Scheduler scheduler) {
        this.delegate = delegate;
        this.scheduler = scheduler;
    }

    @Override
    public Observable<LocalDate> externalCall() {
        return delegate.externalCall()
                .timeout(1, TimeUnit.SECONDS, Observable.empty(), scheduler);
    }
}
