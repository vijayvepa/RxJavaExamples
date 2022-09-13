package rxjava.examples;

import org.junit.jupiter.api.Test;

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
        Book book = personBL.bestBookFor(new Person().hasActivity(true));
        assertEquals("recommended", book.title());
    }

    @Test
    void bestBookForTest_HasNoActivity_returnsBestSeller() {
        Book book = personBL.bestBookFor(new Person().hasActivity(false));
        assertEquals("best-seller", book.title());
    }
}
