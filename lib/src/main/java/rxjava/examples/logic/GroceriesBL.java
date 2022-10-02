package rxjava.examples.logic;

import java.math.BigDecimal;
import java.util.Random;

import static rxjava.examples.Log.log;

public class GroceriesBL {

    private final Random random;

    public GroceriesBL() {
        random = new Random();
    }

    public BigDecimal doPurchase(
            String productName,
            int quantity) {
        log("Purchasing " + quantity + " " + productName);
        //real logic here
        sleep(1000 * quantity);
        int price = random.nextInt(10) + 1;
        int priceForProduct = quantity * price;
        log("Done " + quantity + " " + productName + "(s) \t @ $" + price + " each  |\t $" + priceForProduct);
        return BigDecimal.valueOf(priceForProduct);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
