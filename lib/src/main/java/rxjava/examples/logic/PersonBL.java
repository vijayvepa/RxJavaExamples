package rxjava.examples.logic;

import rx.Observable;
import rxjava.examples.ObservableUtils;
import rxjava.examples.dataaccess.PersonDao;
import rxjava.examples.model.PersonDetails;
import rxjava.examples.model.Person;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PersonBL {
    final PersonDao personDao;

    final Random random;

    public PersonBL(PersonDao personDao) {
        this.personDao = personDao;
        random = new Random();
    }

    public List<PersonDetails> listPeople() {

        List<Person> people = ObservableUtils.toList(personDao.listPeople());

        return people.stream().map(x -> new PersonDetails()).collect(Collectors.toList());
    }

    public List<Person> getSpecifiedNumberOfPeople(int number){
        return ObservableUtils.toList(lazyListPeople(0).take(number));
    }

    public Observable<Person> lazyListPeople(int initialPage){
        return Observable.defer(()-> Observable.from(personDao.listPeople(initialPage)))
                .concatWith(Observable.defer(()-> lazyListPeople(initialPage+1)));
    }

}
