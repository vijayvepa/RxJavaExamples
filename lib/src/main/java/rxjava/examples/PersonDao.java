package rxjava.examples;



import rx.Observable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PersonDao {

    private static final int PAGE_SIZE = 10;

    Observable<Person> listPeople(){
        return Observable.defer(()->Observable.from( query(Queries.SELECT_ALL)));
    }

    @SuppressWarnings({"unused", "SameParameterValue"})
    private List<Person> query(String queryText, Object... params) {
        if(queryText.equals(Queries.SELECT_ALL)) {
            return IntStream.range(0, 1000).mapToObj(i -> new Person().id(i)).collect(Collectors.toList());
        }
        if(queryText.equals(Queries.SELECT_PAGE)){

            int start = (int)params[1];
            int end = start + (int)params[0];

            return IntStream.range(start, end).mapToObj(i->new Person().id(i)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    List<Person> listPeople(int page){
        return query(Queries.SELECT_PAGE, PAGE_SIZE, page * PAGE_SIZE);
    }
}


