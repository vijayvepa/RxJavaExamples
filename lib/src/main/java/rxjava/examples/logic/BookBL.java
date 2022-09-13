package rxjava.examples.logic;

import rx.Observable;
import rxjava.examples.model.CannotRecommendBookException;
import rxjava.examples.ObservableUtils;
import rxjava.examples.model.Book;
import rxjava.examples.model.Person;

public class BookBL {
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
}
