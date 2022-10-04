package rxjava.examples.flowcontrol;

import rx.Observable;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class TradingProducer {

    public Observable<BigDecimal> pricesOf(String ticker) {
        return Observable.interval(50, TimeUnit.MILLISECONDS)
                .flatMap(this::randomDelay)
                .map(this::randomStockPrice)
                .map(BigDecimal::valueOf);
    }

    private Observable<Long> randomDelay(long x) {
        return Observable.just(x).delay((long) (Math.random() * 100), TimeUnit.MILLISECONDS);
    }

    private double randomStockPrice(long x) {
        return 100 + Math.random() * 10 + (Math.sin(x / 100.0)) * 60.0;
    }
}
