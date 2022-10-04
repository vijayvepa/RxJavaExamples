package rxjava.examples.utils;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ObservableUtils {
    public static <T> List<T> toList(Observable<T> observable) {
        return observable.toList().toBlocking().single();
    }

    public static <T> T toObject(Observable<T> observable) {
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
                ///unsubscribe(subscriber, completableFuture);

            });
        });
    }

    /**
     * Unsubscribe
     *
     * @param subscriber subscriber
     * @param future     future
     * @param <T>        type  of observable
     * @deprecated DO NOT USE if just one subscriber decided to cancel, it will affect all subscribers
     */
    @Deprecated
    private static <T> void unsubscribe(Subscriber<? super T> subscriber, CompletableFuture<T> future) {

        subscriber.add(Subscriptions.create(() -> future.cancel(true)));
    }


    public static <T> CompletableFuture<T> toFuture(Observable<T> observable) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        observable
                .single()
                .subscribe(
                        completableFuture::complete,
                        completableFuture::completeExceptionally
                );
        return completableFuture;
    }

    public static <T> CompletableFuture<List<T>> toFutureList(Observable<T> observable) {
        return toFuture(observable.toList());
    }

    public static <T> Observable<T> delayedCompletion() {
        return Observable.<T>empty().delay(1, TimeUnit.SECONDS);
    }
}
