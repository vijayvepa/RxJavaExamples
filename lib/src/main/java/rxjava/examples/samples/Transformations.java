package rxjava.examples.samples;

import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;
import rx.Subscriber;

import java.time.DayOfWeek;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Transformations {

    public static Observable<String> loadRecordsFor(DayOfWeek day) {
        switch (day) {
            case SUNDAY:
                return Observable.interval(90, TimeUnit.MILLISECONDS).take(5).map(i -> "Sun-" + i);
            case MONDAY:
                return Observable.interval(65, TimeUnit.MILLISECONDS).take(5).map(i -> "Mon-" + i);
            case TUESDAY:
                return Observable.interval(75, TimeUnit.MILLISECONDS).take(5).map(i -> "Tue-" + i);
            case WEDNESDAY:
                return Observable.interval(55, TimeUnit.MILLISECONDS).take(5).map(i -> "Wed-" + i);
            case THURSDAY:
                return Observable.interval(25, TimeUnit.MILLISECONDS).take(5).map(i -> "Thu-" + i);
            case FRIDAY:
                return Observable.interval(125, TimeUnit.MILLISECONDS).take(5).map(i -> "Fri-" + i);
            case SATURDAY:
            default:
                return Observable.interval(250, TimeUnit.MILLISECONDS).take(5).map(i -> "Sat-" + i);
        }
    }

    public static Observable<String> stream(int initDelay, int interval, String name) {
        return Observable.interval(initDelay, interval, TimeUnit.MILLISECONDS)
                .map(x -> name + x)
                .doOnSubscribe(() -> System.out.println("Subscribe to " + name))
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe from " + name));
    }

    public static Observable<Integer> randomInts() {
        return Observable.create(s -> {
            Random random = new Random();

            while (!s.isUnsubscribed()) {
                s.onNext(random.nextInt(1000));
            }
        });
    }

    public static Observable<String> speak(String quote, long millisPerChar) {
        String[] tokens = quote.split(" ");

        Observable<String> words = Observable.from(tokens);

        Observable<Long> absoluteDelay = words.map(Transformations::speakLength).map(len -> len * millisPerChar).scan(Long::sum);

        return words.zipWith(absoluteDelay.startWith(0L), Pair::of)
                .flatMap(pair -> Observable.just(pair.getLeft()).delay(pair.getRight(), TimeUnit.MILLISECONDS));
    }

    private static Integer speakLength(String s) {
        return (s.contains(":") || s.contains(",")) ? s.length() + 5 : s.length();
    }

    public static <T> Observable.Transformer<T, T> odd() {
        Observable<Boolean> trueFalse = Observable.just(true, false).repeat();
        return upstream -> upstream.zipWith(trueFalse, Pair::of).filter(Pair::getRight).map(Pair::getLeft);
    }

    public static <T> Observable.Operator<String, T> toStringOfOdd() {
        return new Observable.Operator<String, T>() {

            private boolean odd = true;


            @Override
            public Subscriber<? super T> call(Subscriber<? super String> child) {
                return new Subscriber<T>(child) {
                    @Override
                    public void onCompleted() {
                        child.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        if (odd) {
                            child.onNext(t.toString());
                        } else {
                            request(1);
                        }
                        odd = !odd;
                    }
                };

            }
        };
    }

}

