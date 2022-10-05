package rxjava.examples;

import org.junit.jupiter.api.Test;
import rxjava.examples.flowcontrol.Consumer;
import rxjava.examples.flowcontrol.Producer;

public class BackpressureTests {

    private final Consumer consumer = new Consumer();
    private final Producer producer = new Producer();

    @Test
    void washALotTest() {
        consumer.waitAndWash(
                producer.lotsOfDishesToWash()
        );
    }

    @Test
    void washIndependentlyALotTest() {
        consumer.washIndependently(
                producer.lotsOfDishesToWash()
        );
    }

    @Test
    void washIndependentlyWithoutBreak() {
        consumer.washIndependently(
                producer.lotsOfDishesToWashNoBreak()
        );
    }

    @Test
    void requestExample1() {
        consumer.requestExample(1,
                producer.ranged()
        );
    }

    @Test
    void requestExample3() {
        consumer.requestExample(3,
                producer.ranged()
        );
    }

    @Test
    void backpressureExplicitTest() {
        consumer.backPressureExplicitEnabled(
                producer.lotsOfDishesToWashBackpressureEnabled()
        );
    }


    @Test
    void backpressureLimitTest() {
        consumer.backPressureLimitEnabled(
                producer.lotsOfDishesToWashBackpressureEnabled()
        );
    }

    @Test
    void backpressureDropTest() {
        consumer.backPressureDropEnabled(
                producer.lotsOfDishesToWashBackpressureEnabled()
        );
    }
}
