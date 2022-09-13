package rxjava.examples;

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

        return people.stream().map(x->new PersonDetails()).collect(Collectors.toList());
    }

    Book bestBookFor(Person person){
        try{
            return recommendBook(person);
        }catch (Exception e){
            return bestSeller();
        }
    }

    private Book bestSeller() {
        return new Book().title("best-seller");
    }

    private Book recommendBook(Person person) {
        if(!hasEnoughData(person)){
            throw new CannotRecommendBookException();
        }

        return new Book().title("recommended");
    }

    private boolean hasEnoughData(Person person) {
       return person.hasActivity();
    }


}
