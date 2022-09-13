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
    void getSpecifiedNumberOfPeople_with3Requested(){
        List<Person> specifiedNumberOfPeople = personBL.getSpecifiedNumberOfPeople(3);

        assertEquals(3, specifiedNumberOfPeople.size());
    }

    @Test
    void getSpecifiedNumberOfPeople_with25Requested(){
        List<Person> specifiedNumberOfPeople = personBL.getSpecifiedNumberOfPeople(25);

        assertEquals(25, specifiedNumberOfPeople.size());
    }
}
