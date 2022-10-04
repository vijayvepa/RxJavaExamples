package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.flowcontrol.ComplexConsumer;
import rxjava.examples.flowcontrol.Consumer;
import rxjava.examples.flowcontrol.Producer;
import rxjava.examples.flowcontrol.TradingProducer;
import rxjava.examples.utils.ObservableUtils;

public class FlowControlTests {

    Producer producer = new Producer();
    Consumer consumer = new Consumer();
    ComplexConsumer complexConsumer = new ComplexConsumer("Europe/Warsaw");
    TradingProducer tradingProducer = new TradingProducer();

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

    @Test
    void debounceNamesTest() {

        consumer.debounce(
                ObservableUtils.delayedEmit(producer.names(), producer.delayMillis())
        );
    }

    @Test
    void debouncePricesTest() {

        consumer.debounce(
                ObservableUtils.delayedEmit(producer.prices(), producer.delayMillis())
        );
    }

    @Test
    void debounceConditionalTest() {

        consumer.conditionalDebounce(
                ObservableUtils.delayedEmit(producer.prices(), producer.delayMillis())
        );
    }

    @Test
    void tradingDebounceTest() {

        consumer.conditionalDebounce(
                tradingProducer.pricesOf("NFLX")
        );
    }

    @Test
    void starvingDebounceTest() {
        consumer.starvingDebounce(100,
                producer.intervalObservable(93)
        );
    }

    @Test
    void starvingDebounceWithTimeoutTest() {
        consumer.starvingDebounceWithTimeout(100,
                producer.intervalObservable(95)
        );
    }

    @Test
    void starvingDebounceWithRecoveryTest() {
        consumer.starvingDebounceWithRecoveryBad1(100,
                producer.connectableInterval(93)
        );

    }

    @Test
    void starvingDebounceWithRecoveryBadTest2() {
        consumer.starvingDebounceWithRecoveryBad2(100,
                producer.connectableInterval(93)
        );

    }

    @Test
    void timedDebounceTest() {
        consumer.timedDebounce(100,
                producer.intervalObservable(93)
        ).toBlocking().subscribe(Log::log);

    }
}
