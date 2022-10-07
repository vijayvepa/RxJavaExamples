package rxjava.examples;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.model.Person;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StreamsTests {
    EasyRandom erandom = new EasyRandom();
    Random random = new Random();

    Person getRandomPerson() {

        return erandom.nextObject(Person.class);
    }

    @Test
    void parallelStreamTest() {
        List<Person> personList = Observable.range(1, 1000).map(x -> getRandomPerson()).toList().toBlocking().single();

        List<String> sorted = personList
                .parallelStream()
                .filter(p -> p.age() > 18)
                .map(Person::firstName)
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.toList());

        Log.log(sorted);

    }
}
