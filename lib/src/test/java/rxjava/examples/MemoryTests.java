package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.memory.Picture;

import java.util.concurrent.TimeUnit;

public class MemoryTests {
    @Test
    void test() {
        Observable<Picture> fast = Observable.interval(10, TimeUnit.MICROSECONDS)
                .map(Picture::new);
        Observable<Picture> slow = Observable.interval(11, TimeUnit.MICROSECONDS)
                .map(Picture::new);

        Observable.zip(fast, slow, (f, s) -> f + ":" + s).subscribe(Log::log);
    }

    @Test
    void test2() {
        Observable<Picture> fast = Observable.interval(10, TimeUnit.MICROSECONDS)
                .map(Picture::new);
        Observable<Picture> slow = Observable.interval(11, TimeUnit.MICROSECONDS)
                .map(Picture::new);

        Observable.zip(
                fast.onBackpressureDrop(),
                slow.onBackpressureDrop(),
                (f, s) -> f + ":" + s).subscribe(Log::log);
    }
}
