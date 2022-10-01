package rxjava.examples.samples;

import rx.Observable;

import java.time.DayOfWeek;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OperatorsAndTransformations {

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

    public static Observable<Integer> randomInts(){
        return Observable.create(s->{
            Random random = new Random();

            while (!s.isUnsubscribed()){
                s.onNext(random.nextInt(1000));
            }
        });
    }
}

