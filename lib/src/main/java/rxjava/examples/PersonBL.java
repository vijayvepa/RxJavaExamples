package rxjava.examples;

import java.util.List;
import java.util.stream.Collectors;

public class PersonBL {
    final PersonDao personDao;

    public PersonBL(PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<PersonDetails> listPeople() {

        List<Person> people = ObservableUtils.toList(personDao.listPeople());

        return people.stream().map(x->new PersonDetails()).collect(Collectors.toList());
    }
}
