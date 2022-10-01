package rxjava.examples;

import rx.Observable;

import java.util.List;

public class ObservableUtils {
    public static <T> List<T> toList(Observable<T> observable) {
        return observable.toList().toBlocking().single();
    }

    static <T> T toObject(Observable<T> observable) {
        return observable.toList().toBlocking().single().stream().findFirst().orElse(null);
    }

    public static <T> Observable<T> fromObject(T object) {
        return Observable.just(object);
    }
}
