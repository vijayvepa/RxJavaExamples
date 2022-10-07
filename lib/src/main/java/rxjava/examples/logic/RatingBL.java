package rxjava.examples.logic;

import rx.Observable;
import rxjava.examples.model.Book;
import rxjava.examples.model.Rating;

import java.util.concurrent.TimeUnit;

public class RatingBL {
    public Observable<Rating> fetchRating(Book book) {
        return Observable.just(new Rating()).delay(100, TimeUnit.MILLISECONDS);
    }
}
