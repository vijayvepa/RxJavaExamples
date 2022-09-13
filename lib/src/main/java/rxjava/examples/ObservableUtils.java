package rxjava.examples;

import rx.Observable;

import java.util.Collections;
import java.util.List;

public class ObservableUtils {
    static <T> List<T> toList(Observable<T> observable){
        return observable.toList().toBlocking().single();
    }

    static <T> T toObject(Observable<T> observable){
        return observable.toList().toBlocking().single().stream().findFirst().orElse(null);
    }

    static <T> Observable<T> fromObject(T object){
        return Observable.from(Collections.singletonList(object));
    }
}
