package rxjava.examples.logic;

import rxjava.examples.dataaccess.PersonDao;
import rxjava.examples.model.Person;
import rxjava.examples.model.PersonDetails;
import rxjava.examples.utils.ObservableUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PersonBL {
    final PersonDao personDao;
    final PersonRxBL personRxBL;
    final Random random;

    public PersonBL(PersonDao personDao) {
        this.personDao = personDao;
        personRxBL = new PersonRxBL(personDao);
        random = new Random();
    }

    public List<PersonDetails> listPeople() {
        List<Person> people = ObservableUtils.toList(personDao.listPeople());
        return people.stream().map(x -> new PersonDetails()).collect(Collectors.toList());
    }

    public List<Person> getSpecifiedNumberOfPeopleRecursive(int number) {
        return ObservableUtils.toList(personRxBL.getAllPeopleRecursive(0).take(number));
    }

    public List<Person> getSpecifiedNumberOfPeopleAlt(int number) {
        return ObservableUtils.toList(personRxBL.getAllPeopleConcatMap().take(number));
    }


}
