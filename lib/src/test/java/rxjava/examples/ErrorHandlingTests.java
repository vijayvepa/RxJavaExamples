package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.errorhandling.Timeouts;

public class ErrorHandlingTests {

    Timeouts timeouts = new Timeouts();

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
}
