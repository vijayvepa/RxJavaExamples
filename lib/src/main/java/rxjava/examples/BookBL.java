package rxjava.examples;

import rx.Observable;

public class BookBL {
    Observable<Book> bestBookFor(Person person) {
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