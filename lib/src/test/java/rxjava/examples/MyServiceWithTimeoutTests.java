package rxjava.examples;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rxjava.examples.testingsupport.MyService;
import rxjava.examples.testingsupport.MyServiceWithTimeout;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doReturn;

public class MyServiceWithTimeoutTests {
    private MyServiceWithTimeout mockReturning(
            Observable<LocalDate> result,
            TestScheduler scheduler
    ) {
        MyService mock = Mockito.mock(MyService.class);

        doReturn(result).when(mock).externalCall();

        return new MyServiceWithTimeout(mock, scheduler);
    }

    @Test
    void timeoutWhenServiceNeverCompletes() {
        TestScheduler testScheduler = Schedulers.test();
        MyService mock = mockReturning(Observable.never(), testScheduler);
        TestSubscriber<LocalDate> testSubscriber = new TestSubscriber<>();

        //when
        mock.externalCall().subscribe(testSubscriber);

        //then
        testScheduler.advanceTimeBy(950, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoTerminalEvent();

        testScheduler.advanceTimeBy(950, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();

        testSubscriber.assertNoValues();
    }

    @Test
    void valueIsReturnedJustBeforeTimeout() {
        TestScheduler testScheduler = Schedulers.test();

        final Observable<LocalDate> slow = Observable.timer(950, TimeUnit.MILLISECONDS, testScheduler)
                .map(x -> LocalDate.now());

        MyService mock = mockReturning(slow, testScheduler);
        TestSubscriber<LocalDate> testSubscriber = new TestSubscriber<>();

        //when
        mock.externalCall().subscribe(testSubscriber);

        //then
        testScheduler.advanceTimeBy(930, TimeUnit.MILLISECONDS);
        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();

        testScheduler.advanceTimeBy(50, TimeUnit.MILLISECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);

    }


}
