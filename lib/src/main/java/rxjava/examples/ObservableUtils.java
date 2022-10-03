package rxjava.examples;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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


    public static <T> Observable<T> fromCompletableFuture(CompletableFuture<T> completableFuture) {
        return Observable.<T>create(subscriber -> {
            completableFuture.whenComplete((value, exception) -> {
                if (exception != null) {
                    subscriber.onError(exception);
                } else {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                }

                //don't do this
                //unsubscribe(subscriber, completableFuture);

            });
        });
    }

    private static <T> void unsubscribe(Subscriber<? super T> subscriber, CompletableFuture<T> future) {
        //if just one subscriber decided to cancel, it will affect all subscribers
        subscriber.add(Subscriptions.create(() -> future.cancel(true)));
    }

}
