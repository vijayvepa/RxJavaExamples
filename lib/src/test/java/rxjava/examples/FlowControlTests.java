package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.flowcontrol.Consumer;
import rxjava.examples.flowcontrol.Producer;
import rxjava.examples.utils.ObservableUtils;

public class FlowControlTests {

    Producer producer = new Producer();
    Consumer consumer = new Consumer();

    @Test
    void sampling() {
        consumer.samplingTimeStampedObservable(
                producer.observableAt10msFrequency()
        );
    }

    @Test
    void samplingNames() {
        consumer.samplingStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis())
        );

    }

    @Test
    void samplingNamesDynamic() {
        consumer.samplingDynamicStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis())
        );

    }


    @Test
    void samplingNamesWithCompletionDelay() {
        consumer.samplingStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis()).concatWith(ObservableUtils.delayedCompletion())
        );
    }

    @Test
    void throttleFirst_Names() {
        consumer.throttleFirstStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis())
        );

    }

    @Test
    void throttleFirst_NamesWithCompletionDelay() {
        consumer.throttleFirstStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis()).concatWith(ObservableUtils.delayedCompletion())
        );

    }


    @Test
    void throttleLast_Names() {
        consumer.throttleLastStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis())
        );

    }

    @Test
    void throttleLast_NamesWithCompletionDelay() {
        consumer.throttleLastStringObservable(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis()).concatWith(ObservableUtils.delayedCompletion())
        );

    }

    @Test
    void bufferedTest() {
        consumer.buffered(
                producer.ranged()
        );
    }

    @Test
    void bufferedWithOverlapTest() {
        consumer.bufferedWithOverlap(
                producer.ranged()
        );
    }

    @Test
    void movingAverageTest() {
        consumer.movingAverage(
                producer.randomGaussian()
        );
    }

    @Test
    void bufferWithSkipTest() {
        consumer.bufferedWithSkip(
                producer.ranged()
        );
    }

    @Test
    void bufferWithSkipAndFlatMapTest() {
        consumer.bufferedWithSkipAndFlat(
                producer.ranged()
        );
    }

    @Test
    void bufferWithTimeSpanTest() {
        consumer.bufferWithTimePeriod(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis())
        );
    }


}
