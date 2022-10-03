package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.logic.BookBL;
import rxjava.examples.model.Book;
import rxjava.examples.model.Person;
import rxjava.examples.utils.ObservableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @SuppressWarnings("Convert2MethodRef")
    @Test
    void bookBLGetOrderBookLength_randomTest() {
        List<Integer> bookLengths = new ArrayList<>();

        IntStream.range(0, 10).forEach(i -> {
                    bookBL.incrementBookLength();
                    bookLengths.add(bookBL.getOrderBookLength());
                }
        );

        String joinedString = bookLengths.stream().map(x -> x.toString()).collect(Collectors.joining(","));
        System.out.println(joinedString);
        assertNotNull(joinedString);
    }
}
