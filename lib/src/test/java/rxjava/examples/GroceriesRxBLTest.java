package rxjava.examples;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.logic.GroceriesBL;
import rxjava.examples.logic.GroceriesRxBL;

import java.math.BigDecimal;

public class GroceriesRxBLTest {

    private final GroceriesRxBL groceriesRxBL = new GroceriesRxBL();

    private final GroceriesBL groceriesBL = new GroceriesBL();
    private final SampleSchedulers sampleSchedulers = new SampleSchedulers();

    @Test
    void testGroceries() throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(sampleSchedulers.schedulerA)  //BROKEN!!!
                .map(prod -> groceriesBL.doPurchase(prod, 1))
                .reduce(BigDecimal::add)
                .single();
        totalPrice.subscribe(x->System.out.println("Total: $" + x));
        Thread.sleep(10000);
    }

    @Test
    void testGroceriesFlatMap() throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(sampleSchedulers.schedulerA)  //BROKEN!!!
                .flatMap(prod -> groceriesRxBL.purchase(prod, 1))
                .reduce(BigDecimal::add)
                .single();
        totalPrice.subscribe(x->System.out.println("Total: $" + x));
        Thread.sleep(10000);
    }

    @Test
    void testGroceriesConcurrentFlatMap() throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")

                .flatMap(prod -> groceriesRxBL.purchase(prod, 1)
                        .subscribeOn(sampleSchedulers.schedulerA))
                .reduce(BigDecimal::add)
                .single();
        totalPrice.subscribe(x->System.out.println("Total: $" + x));
        Thread.sleep(10000);
    }
    @Test
    void testGroceriesWithDuplicates() throws InterruptedException {
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "egg", "milk", "tomato",
                        "cheese", "tomato", "egg", "egg")
                .groupBy(prod -> prod)
                .flatMap(grouped -> grouped
                        .count()
                        .map(quantity -> {
                            String productName = grouped.getKey();
                            return Pair.of(productName, quantity);
                        }))
                .flatMap(order -> groceriesRxBL
                        .purchase(order.getKey(), order.getValue())
                        .subscribeOn(sampleSchedulers.schedulerB))
                .reduce(BigDecimal::add)
                .single();
        totalPrice.subscribe(x->System.out.println("Total: $" + x));
        Thread.sleep(10000);
    }
}
