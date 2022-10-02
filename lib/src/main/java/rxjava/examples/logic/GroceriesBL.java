package rxjava.examples.logic;

import rxjava.examples.Log;

import java.math.BigDecimal;
import java.util.Random;

public class GroceriesBL {

    private final Random random;

    public GroceriesBL() {
        random = new Random();
    }

    BigDecimal doPurchase(String productName, int quantity) throws InterruptedException {
        Log.threadLog("Purchasing " + quantity + " " + productName + "(s)");
        Thread.sleep(1000);
        int priceForProduct = random.nextInt(10) * quantity;
        Log.threadLog("Done " + quantity + " " + productName + "(s)");
        return BigDecimal.valueOf(priceForProduct);
    }
}
