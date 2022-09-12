package rxjava.examples;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonBLTest {
    PersonBL personBL = new PersonBL(new PersonDao());


    @Test
    void listPeopleTest(){
        List<PersonDetails> personDetails = personBL.listPeople();
        assertEquals(1000, personDetails.size());
    }
}
