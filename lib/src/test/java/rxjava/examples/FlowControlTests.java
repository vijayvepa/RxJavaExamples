package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.samples.FlowControl;
import rxjava.examples.utils.ObservableUtils;

public class FlowControlTests {

    FlowControl flowControl = new FlowControl();

    @Test
    void sampling() {
        flowControl.sampleAtOneSecond();
    }

    @Test
    void samplingNames() {
        flowControl.sampleAtOneSecond(flowControl.delayedNames());
    }

    @Test
    void samplingNamesWithCompletionDelay() {
        final Observable<String> delayedCompletion = flowControl.delayedNames().concatWith(ObservableUtils.delayedCompletion());
        flowControl.sampleAtOneSecond(delayedCompletion);
    }
}
