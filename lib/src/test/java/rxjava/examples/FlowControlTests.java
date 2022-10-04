package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.flowcontrol.ComplexConsumer;
import rxjava.examples.flowcontrol.Consumer;
import rxjava.examples.flowcontrol.Producer;
import rxjava.examples.utils.ObservableUtils;

public class FlowControlTests {

    Producer producer = new Producer();
    Consumer consumer = new Consumer();

    ComplexConsumer complexConsumer = new ComplexConsumer("Europe/Warsaw");

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

    @Test
    void complexBufferingTest() {
        complexConsumer.complexBuffering(
                producer.teleData()
        );
    }

    @Test
    void complexBufferingNonBusinessHoursTest() {
        new ComplexConsumer("Asia/Shanghai").complexBuffering(
                producer.teleData()
        );
    }

    @Test
    void windowBufferingTest() {
        consumer.windowBuffering(
                producer.teleData()
        );
    }

}
