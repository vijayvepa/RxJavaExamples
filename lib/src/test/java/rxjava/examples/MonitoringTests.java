package rxjava.examples;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class MonitoringTests {

    private Instant dbQuery() throws InterruptedException {
        Log.log("DBQuery");
        Thread.sleep(100);
        return Instant.now();
    }

    @Test
    void monitoringTest() {
        Observable<Instant> timestamps = Observable.just(Instant.now(), Instant.now().plusSeconds(1), Instant.now().plusSeconds(3))
                .doOnSubscribe(() -> Log.log("INFO: subscribe()"));

        timestamps.zipWith(timestamps.skip(1), Duration::between)
                .map(Object::toString)
                .subscribe(Log::log);
    }

    @Test
    void runtimeTest() {
        Observable<String> obs = Observable
                .<String>error(new RuntimeException("Swallowed"))
                .doOnError(throwable -> Log.log("Exception:" + throwable))
                .onErrorReturn(throwable -> "Fallback");

        obs.subscribe(Log::log);
    }

    @Test
    void metricsTest() {
        MetricRegistry metricRegistry = new MetricRegistry();
        Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger(SomeClass.class))
                .build();
        reporter.start(1, TimeUnit.SECONDS);

        Counter items = metricRegistry.counter("items");
        Observable.just(1).repeat()
                .doOnNext(x -> items.inc())
                .subscribe(Log::log);
    }
}
