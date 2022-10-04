package rxjava.examples.flowcontrol;

import rx.Observable;
import rxjava.examples.Log;
import rxjava.examples.model.TeleData;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ComplexConsumer {

    private final String zoneId;

    private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);

    public ComplexConsumer(String zoneId) {
        this.zoneId = zoneId;
    }

    private boolean isBusinessHour(long timestamp) {
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);

        LocalTime localTime = zonedDateTime.toLocalTime();

        return !localTime.isBefore(BUSINESS_START) &&
                !localTime.isAfter(BUSINESS_END);
    }

    /**
     * Inside Business Hours: Take 100 millisecond snapshots every second
     *
     * @return duration
     */
    private Observable<Duration> insideBusinessHours() {
        return Observable.interval(1, TimeUnit.SECONDS).filter(this::isBusinessHour).map(x -> Duration.ofMillis(100));
    }

    /**
     * Outside Business Hours: Take 200 millisecond snapshots every 5 seconds
     *
     * @return duration
     */
    private Observable<Duration> outsideBusinessHours() {
        return Observable.interval(5, TimeUnit.SECONDS).filter(x -> !this.isBusinessHour(x)).map(x -> Duration.ofMillis(200));
    }

    private Observable<Duration> bufferedOpenings() {
        return Observable.merge(insideBusinessHours(), outsideBusinessHours());
    }

    public void complexBuffering(Observable<TeleData> observable) {
        observable.buffer(bufferedOpenings()).map(List::size).toBlocking().subscribe(Log::log);
    }

}
