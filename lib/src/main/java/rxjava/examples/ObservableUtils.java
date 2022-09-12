package rxjava.examples;

import rx.Observable;

import java.util.List;

public class ObservableUtils {
    static <T> List<T> toList(Observable<T> observable){
        return observable.toList().toBlocking().single();
    }
}
