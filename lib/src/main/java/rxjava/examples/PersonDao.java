package rxjava.examples;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PersonDao {

    List<Person> listPeople(){
        return query("SELECT * FROM PEOPLE");
    }

    private List<Person> query(String s) {
        return IntStream.range(0, 1000).mapToObj(i-> new Person()).collect(Collectors.toList());
    }
}


