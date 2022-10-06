package rxjava.examples.testingsupport;

import rx.Observable;
import rx.observables.SyncOnSubscribe;

public class NaturalNumbers {

    public Observable<Long> naturalNumber1() {
        return Observable.unsafeCreate(subscriber -> {
            long i = 0;
            while (!subscriber.isUnsubscribed()) {
                subscriber.onNext(i++);
            }
        });
    }

    public Observable<Long> naturalNumber2() {
        return Observable.unsafeCreate(SyncOnSubscribe.createStateful(() -> 0L, (cur, observer) -> {
            observer.onNext(cur);
            return cur + 1;
        }));
    }
}
