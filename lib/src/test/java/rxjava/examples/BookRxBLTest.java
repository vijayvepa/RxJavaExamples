package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rxjava.examples.logic.BookBL;
import rxjava.examples.logic.BookRxBL;

import java.util.stream.IntStream;

public class BookRxBLTest {

    private final BookBL bookBL = new BookBL();
    private final BookRxBL bookRxBL = new BookRxBL(bookBL);

    @Test
    void bookBLGetOrderBookLength_randomTest() throws InterruptedException {

        Subscription subscribe = bookRxBL.getOrderBookLength().subscribe(x -> {
            System.out.println("Received" + x.toString());
        });


        IntStream.range(0, 10).forEach(i -> {

                    System.out.println("Increment book length");
                    bookBL.incrementBookLength();
                    bookBL.getOrderBookLength();
                }
        );


    }

    @Test
    void bookBLSynchronousObservableTest() throws InterruptedException {
        Observable<Integer> orderBookLength = bookRxBL.getOrderBookLength();
        orderBookLength.map(x -> "Subscribe Map to " + x).subscribeOn(Schedulers.io()).subscribe(System.out::println);


        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                System.out.println("Increment book length");
                bookBL.incrementBookLength();
                int bookLength = bookBL.getOrderBookLength();
                System.out.println("Booklength " + bookLength + " on " + Thread.currentThread());
            }).start();
            sleep(5);
        });

        Thread.sleep(100);

    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
