package rxjava.examples;



import rx.Observable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PersonDao {

    Observable<Person> listPeople(){
        return Observable.from( query("SELECT * FROM PEOPLE"));
    }

    @SuppressWarnings({"unused", "SameParameterValue"})
    private List<Person> query(String s) {
        return IntStream.range(0, 1000).mapToObj(i-> new Person()).collect(Collectors.toList());
    }
}


