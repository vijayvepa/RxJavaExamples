package rxjava.examples.logic;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class BookRxBL {
    private final BookBL bookBL;

    public BookRxBL(BookBL bookBL) {
        this.bookBL = bookBL;
    }

    public Observable<Integer> getOrderBookLength() {
        return Observable.interval(1, TimeUnit.MILLISECONDS)
                .map(x -> bookBL.getOrderBookLength())
                .distinctUntilChanged();
    }
}
