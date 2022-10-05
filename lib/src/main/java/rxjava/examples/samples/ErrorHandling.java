package rxjava.examples.samples;

import rx.Observable;

public class ErrorHandling {

    Observable<Integer> divBy(int top, int bottom) {
        return Observable.unsafeCreate(subscriber -> {
            try {
                subscriber.onNext(top / bottom);
            } catch (Exception ex) {
                subscriber.onError(ex);
            }
        });
    }

    Observable<Integer> divBy2(int top, int bottom) {
        return Observable.unsafeCreate(subscriber -> subscriber.onNext(top / bottom));
    }

    Observable<Integer> divBy3(int top, int bottom) {
        return Observable.fromCallable(() -> top / bottom);
    }

    void divBy4() {
        Observable.just(1, 0).map(x -> 10 / x);
    }

    void divBy4Handled() {
        Observable.just(1, 0)
                .flatMap(x -> x == 0 ? Observable.error(new ArithmeticException("Zero: -(")) : Observable.just(10 / x));
    }

    void npe() {
        Observable.just("Lorem", null, "ipsum").filter(String::isEmpty);
    }
}
