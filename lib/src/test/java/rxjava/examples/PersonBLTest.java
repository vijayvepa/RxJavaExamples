package rxjava.examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rxjava.examples.dataaccess.PersonDao;
import rxjava.examples.logic.PersonBL;
import rxjava.examples.model.Person;
import rxjava.examples.model.PersonDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PersonBLTest {

    @Spy
    PersonDao personDao;

    @InjectMocks
    PersonBL personBL;


    @Test
    void listPeopleTest() {
        List<PersonDetails> personDetails = personBL.listPeople();
        assertEquals(1000, personDetails.size());
    }


    @Test
    void getSpecifiedNumberOfPeople_with3Requested(){
        List<Person> specifiedNumberOfPeople = personBL.getSpecifiedNumberOfPeople(3);

        assertEquals(3, specifiedNumberOfPeople.size());
        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(personDao, Mockito.times(1)).listPeople(pageCaptor.capture());
        assertEquals(1, pageCaptor.getAllValues().size());
    }

    @Test
    void getSpecifiedNumberOfPeople_with25Requested(){
        List<Person> specifiedNumberOfPeople = personBL.getSpecifiedNumberOfPeople(25);

        assertEquals(25, specifiedNumberOfPeople.size());
        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(personDao, Mockito.times(3)).listPeople(pageCaptor.capture());
        assertEquals(3, pageCaptor.getAllValues().size());
    }
}
