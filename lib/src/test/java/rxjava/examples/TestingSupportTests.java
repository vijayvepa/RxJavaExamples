package rxjava.examples;

import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import rx.Notification;
import rx.Observable;
import rx.exceptions.CompositeException;
import rx.observables.BlockingObservable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestingSupportTests {

    @Test
    void virtualTimeTest() throws InterruptedException {

        TestScheduler testScheduler = Schedulers.test();

        Observable<String> fast = Observable.interval(10, TimeUnit.MILLISECONDS, testScheduler)
                .map(x -> "F" + x)
                .take(3);

        Observable<String> slow = Observable.interval(50, TimeUnit.MILLISECONDS, testScheduler)
                .map(x -> "S" + x)
                .take(3);

        Observable<String> concat = Observable.concat(fast, slow);
        concat.subscribe(Log::log);
        Log.log("Subscribed");

        TimeUnit.SECONDS.sleep(1);
        Log.log("After 1 second");
        testScheduler.advanceTimeBy(25, TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(1);
        Log.log("After 1 more second");
        testScheduler.advanceTimeBy(75, TimeUnit.MILLISECONDS);

        TimeUnit.SECONDS.sleep(1);
        Log.log("After yet 1 more second");
        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS);


    }

    @Test
    void shouldApplyConcatMapInOrder() throws Exception {
        List<String> list = Observable.range(1, 3)
                .concatMap(x -> Observable.just(x, -x))
                .map(Object::toString)
                .toList()
                .toBlocking()
                .single();

        assertThat(list).containsExactly("1", "-1", "2", "-2", "3", "-3");

    }

    @Test
    void testErrorCondition() {
        File file = new File("404.txt");

        final BlockingObservable<String> fileContents = Observable.fromCallable(() -> Files.asCharSource(file, StandardCharsets.UTF_8).read())
                .toBlocking();

        final RuntimeException runtimeException = assertThrows(RuntimeException.class, fileContents::single);
        assertThat(runtimeException).hasCauseInstanceOf(FileNotFoundException.class);
    }

    @Test
    void testConcatMapDelayErrorMaterialize() {
        final Observable<Notification<Integer>> notificationObservable = Observable.just(3, 0, 2, 0, 1, 0)
                .concatMapDelayError(x -> Observable.fromCallable(() -> 100 / x))
                .materialize();

        final List<Notification.Kind> kinds = notificationObservable.map(Notification::getKind).toList()
                .toBlocking().single();

        assertThat(kinds).containsExactly(
                Notification.Kind.OnNext,
                Notification.Kind.OnNext,
                Notification.Kind.OnNext,
                Notification.Kind.OnError
        );
    }

    @Test
    void testConcatMapDelayError() {
        final Observable<Integer> integerObservable = Observable.just(3, 0, 2, 0, 1, 0)
                .concatMapDelayError(x -> Observable.fromCallable(() -> 100 / x));

        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        integerObservable.subscribe(testSubscriber);

        testSubscriber.assertValues(33, 50, 100);
        testSubscriber.assertError(CompositeException.class);
    }


}
