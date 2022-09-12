package rxjava.examples;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonDaoTest {
    @Test
    void listPeopleTest(){
        List<Person> people = new PersonDao().listPeople();
        assertEquals(1000, people.size());
    }
}
