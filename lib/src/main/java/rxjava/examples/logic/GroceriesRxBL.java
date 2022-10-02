package rxjava.examples.logic;

import rx.Observable;

import java.math.BigDecimal;

public class GroceriesRxBL {
    private final GroceriesBL groceriesBL;

    public GroceriesRxBL() {
        groceriesBL = new GroceriesBL();
    }

    public Observable<BigDecimal> purchase(String productName, int quantity) {
        return Observable.fromCallable(() ->
                groceriesBL.doPurchase(productName, quantity));
    }
}
