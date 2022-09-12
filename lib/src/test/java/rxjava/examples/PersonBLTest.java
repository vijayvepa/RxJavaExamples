package rxjava.examples;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonBLTest {
    PersonBL personBL = new PersonBL(new PersonDao());


    @Test
    void listPeopleTest() throws ExecutionException, InterruptedException {
        List<PersonDetails> personDetails = personBL.listPeople();
        assertEquals(1000, personDetails.size());
    }
}
