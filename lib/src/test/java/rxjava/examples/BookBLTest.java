package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookBLTest {

    private final BookBL bookBL = new BookBL();

    @Test
    void bestBookForTest_HasActivity_returnsRecommended() {
        Observable<Book> book = bookBL.bestBookFor(new Person().hasActivity(true));

        book.map(Book::title).subscribe(title -> assertEquals("recommended", title));
    }


    @Test
    void bestBookForTest_HasNoActivity_returnsBestSeller() {
        Observable<Book> book = bookBL.bestBookFor(new Person().hasActivity(false));
        Book book1 = ObservableUtils.toObject(book);

        assertEquals("best-seller", book1.title());
    }
}
