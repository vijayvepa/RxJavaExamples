package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;

import static rxjava.examples.Log.log;

public class ReactiveExtensionsTests {

    @Test
    public void rangeTest(){
        log("Before");
        Observable.range(5,3).subscribe(Log::log);
        log("After");
    }

    @Test
    public void lowLevelRangeTest(){
        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(5);
                        subscriber.onNext(6);
                        subscriber.onNext(7);
                        subscriber.onCompleted();
                        log("Complete");
                    }
                });

        log("Starting");
        ints.subscribe(i-> log("Element" + i) );
        log("Exit");
    }


    @Test
    public void multipleSubscribersTest(){
        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(32);
                        log("Complete");
                    }
                });

        log("Starting");
        ints.subscribe(i-> log("Element A:" + i) );
        ints.subscribe(i-> log("Element B:" + i) );
        log("Exit");
    }

    @Test
    public void multipleSubscribersWithCacheTest(){
        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(32);
                        log("Complete");
                    }
                }).cache();

        log("Starting");
        ints.subscribe(i-> log("Element A:" + i) );
        ints.subscribe(i-> log("Element B:" + i) );
        log("Exit");
    }
}
