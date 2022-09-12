package rxjava.examples;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonDaoTest {
    private final PersonDao personDao = new PersonDao();
    @Test
    void listPeopleTest() {
        List<Person> people = ObservableUtils.toList(personDao.listPeople());
        assertEquals(1000, people.size());
    }
}
