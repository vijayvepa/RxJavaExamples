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

    @Test
    void listPeople_withPage0_Test(){
        List<Person> people = personDao.listPeople(0);

        assertEquals(10, people.size());
        assertEquals(5, people.get(5).id());
    }

    @Test
    void listPeople_withPage1_Test(){
        List<Person> people = personDao.listPeople(1);

        assertEquals(10, people.size());
        assertEquals(15, people.get(5).id());
    }
}
