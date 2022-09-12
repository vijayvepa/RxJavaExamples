package rxjava.examples;


import io.reactivex.Observable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PersonDao {

    Observable<Person> listPeople(){
        return Observable.fromIterable( query("SELECT * FROM PEOPLE"));
    }

    private List<Person> query(String s) {
        return IntStream.range(0, 1000).mapToObj(i-> new Person()).collect(Collectors.toList());
    }
}


