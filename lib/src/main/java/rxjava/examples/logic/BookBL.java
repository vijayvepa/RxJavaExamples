package rxjava.examples.logic;

import rx.Observable;
import rxjava.examples.model.Book;
import rxjava.examples.model.CannotRecommendBookException;
import rxjava.examples.model.Person;
import rxjava.examples.utils.ObservableUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BookBL {

    private int bookLength;

    private final Random random;

    public BookBL() {
        random = new Random();
        bookLength = 1;
    }

    public Observable<Book> allBooks() {
        return Observable.interval(300, TimeUnit.MILLISECONDS).zipWith(Observable.just(new Book()).repeat(), (x, y) -> y);
    }

    public Observable<Book> bestBookFor(Person person) {
        return tryRecommendBook(person).onErrorResumeNext(bestSeller());
    }

    private Observable<Book> bestSeller() {
        return ObservableUtils.fromObject(new Book().title("best-seller"));
    }


    private Observable<Book> tryRecommendBook(Person person) {
        return Observable.defer(() -> ObservableUtils.fromObject(recommendBook(person)));
    }

    private Book recommendBook(Person person) {
        if (!hasEnoughData(person)) {
            throw new CannotRecommendBookException();
        }

        return new Book().title("recommended");
    }

    private boolean hasEnoughData(Person person) {
        return person.hasActivity();
    }

    public void incrementBookLength() {
        bookLength += random.nextInt(5);
    }

    public int getOrderBookLength() {
        return bookLength;
    }
}
