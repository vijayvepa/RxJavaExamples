package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonBLTest {
    PersonBL personBL = new PersonBL(new PersonDao());


    @Test
    void listPeopleTest() {
        List<PersonDetails> personDetails = personBL.listPeople();
        assertEquals(1000, personDetails.size());
    }

    @Test
    void bestBookForTest_HasActivity_returnsRecommended() {
        Observable<Book> book = personBL.bestBookFor(new Person().hasActivity(true));

        book.map(Book::title).subscribe(title -> assertEquals("recommended", title));
    }




    @Test
    void bestBookForTest_HasNoActivity_returnsBestSeller() {
        Observable<Book> book = personBL.bestBookFor(new Person().hasActivity(false));
        Book book1  = ObservableUtils.toObject(book);

        assertEquals("best-seller", book1.title());
    }
}
