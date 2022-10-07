package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.hystrix.BlockingCommand;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HystrixTests {

    @Test
    void executeTest() {
        String string = new BlockingCommand().execute();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void queueTest() throws ExecutionException, InterruptedException {
        String string = new BlockingCommand().queue().get();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void observeTest() {
        final String string = new BlockingCommand().observe().toBlocking().single();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void observableTest() {
        final String string = new BlockingCommand().toObservable().toBlocking().single();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void observableWithOperationsTest() {
        new BlockingCommand().toObservable().doOnError(ex -> Log.log("WARN:" + ex))
                .retryWhen(ex -> ex.delay(500, TimeUnit.MILLISECONDS))
                .timeout(3, TimeUnit.SECONDS)
                .toBlocking()
                .subscribe(Log::log);

    }
}
