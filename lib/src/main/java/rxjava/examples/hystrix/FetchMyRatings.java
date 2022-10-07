package rxjava.examples.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rxjava.examples.logic.RatingBL;
import rxjava.examples.model.Book;
import rxjava.examples.model.Rating;

import java.util.Collection;

public class FetchMyRatings extends HystrixObservableCommand<Rating> {
    private final Collection<Book> books;
    private final RatingBL ratingBL;

    public FetchMyRatings(Collection<Book> books, RatingBL ratingBL) {
        super(HystrixCommandGroupKey.Factory.asKey("Books"));
        this.books = books;
        this.ratingBL = ratingBL;
    }


    @Override
    protected Observable<Rating> construct() {
        return ratingBL.fetchRatings(books);
    }
}
