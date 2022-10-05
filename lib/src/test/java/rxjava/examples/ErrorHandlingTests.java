package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.errorhandling.Retries;
import rxjava.examples.errorhandling.Timeouts;

public class ErrorHandlingTests {

    Timeouts timeouts = new Timeouts();
    Retries retries = new Retries();

    @Test
    void timeoutTest() throws InterruptedException {
        timeouts.timeoutExample(210);
        Thread.sleep(5000);
    }

    @Test
    void timeoutTest2() throws InterruptedException {
        timeouts.timeoutExample(190);
        Thread.sleep(5000);
    }

    @Test
    void timeoutWithHandshakeTest() throws InterruptedException {
        timeouts.timeoutExampleWithHandshake();
        Thread.sleep(5000);
    }

    @Test
    void trackLatencyTest() throws InterruptedException {
        timeouts.trackLatency();
    }

    @Test
    void timeoutExampleWithFallback() {
        timeouts.timeoutExampleWithFallback();
    }

    @Test
    void retryExample() {
        retries.retryExample();
    }

    @Test
    void retryWithLimitExample() {
        retries.retryWithLimitExample();
    }

    @Test
    void retryWithLimitAltExample() {
        retries.retryWithLimitAltExample();
    }

    @Test
    void retryWithDropExample() {
        retries.retryWithDropExample();
    }

    @Test
    void retryWithBackoff() {
        retries.retryWithSimpleBackoffDelay();
    }

    @Test
    void retryWithExpBackoff() {
        retries.retryWithExponentialBackoff(20);
    }
}
