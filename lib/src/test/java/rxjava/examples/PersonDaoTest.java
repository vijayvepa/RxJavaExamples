package rxjava.examples;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonDaoTest {
    @Test
    void listPeopleTest() throws ExecutionException, InterruptedException {
        List<Person> people = new PersonDao().listPeople().toList().blockingGet();
        assertEquals(1000, people.size());
    }
}
