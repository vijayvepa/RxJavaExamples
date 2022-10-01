package rxjava.examples.logic;

import rx.Observable;
import rxjava.examples.dataaccess.PersonDao;
import rxjava.examples.model.Person;

import java.util.Random;

public class PersonRxBL {
    final PersonDao personDao;

    final Random random;

    public PersonRxBL(PersonDao personDao) {
        this.personDao = personDao;
        random = new Random();
    }

    public Observable<Person> getAllPeopleConcatMap() {

        return Observable.range(0, Integer.MAX_VALUE)
                .map(personDao::listPeopleByPage)
                .takeWhile(list -> !list.isEmpty())
                .concatMap(Observable::from);

    }

    public Observable<Person> getAllPeopleRecursive(int initialPage) {
        return
                Observable.defer(() -> Observable.from(personDao.listPeopleByPage(initialPage)))
                        .concatWith(Observable.defer(() -> getAllPeopleRecursive(initialPage + 1)));
    }
}
