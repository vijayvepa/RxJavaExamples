package rxjava.examples.logic;

import rx.Observable;
import rxjava.examples.model.Book;
import rxjava.examples.model.Rating;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RatingBL {
    public Observable<Rating> fetchRating(Book book) {
        return Observable.just(new Rating()).delay(100, TimeUnit.MILLISECONDS);
    }

    public Observable<Rating> fetchRatings(Collection<Book> books) {
        final List<Rating> ratings = books.stream().map(x -> new Rating()).collect(Collectors.toList());
        return Observable.from(ratings).delay(200, TimeUnit.MILLISECONDS);
    }
}
