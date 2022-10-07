package rxjava.examples.memory;

import rx.Observable;
import rxjava.examples.Log;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IncidentDriver {

    public static void main(String[] args) {
        bufferExample();
    }

    private static void bufferExample() {
        Random random = new Random();
        Observable<Incident> incidents = Observable.range(1, Integer.MAX_VALUE).flatMap(x -> Observable.just(new Incident().setHighPriority(random.nextBoolean())).delay(10, TimeUnit.MILLISECONDS));

        incidents.buffer(1, TimeUnit.SECONDS)
                .map((List<Incident> oneSecond) -> oneSecond.stream().filter(Incident::isHighPriority).count() > 5)
                .subscribe(Log::log);

    }

    private static void windowExample() {
        Random random = new Random();
        Observable<Incident> incidents = Observable.range(1, Integer.MAX_VALUE).flatMap(x -> Observable.just(new Incident().setHighPriority(random.nextBoolean())).delay(10, TimeUnit.MILLISECONDS));

        incidents
                .window(1, TimeUnit.SECONDS)
                .flatMap((Observable<Incident> oneSecond) ->
                        oneSecond.filter(Incident::isHighPriority)
                                .count()
                                .map(c -> (c > 5))
                )
                .subscribe(Log::log);

    }


}
