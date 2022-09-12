package rxjava.examples;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PersonBL {
    final PersonDao personDao;

    public PersonBL(PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<PersonDetails> listPeople() throws ExecutionException, InterruptedException {

        List<Person> people = personDao.listPeople().toList().blockingGet();

        return people.stream().map(x->new PersonDetails()).collect(Collectors.toList());
    }
}
