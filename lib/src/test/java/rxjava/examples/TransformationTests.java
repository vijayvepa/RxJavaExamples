package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.model.*;
import rxjava.examples.samples.OperatorsAndTransformations;
import rxjava.examples.utils.MorseCodeUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class TransformationTests {

    @Test
    void testMap() {
        Observable<String> filter = Observable.just(8, 9, 10).filter(i -> i % 3 > 0).map(i -> "$" + i * 10).filter(s -> s.length() < 4);

        filter.subscribe(System.out::println);
    }

    @Test
    void testMapWithLogging() {
        Observable.just(8, 9, 10)
                .doOnNext(i -> System.out.println("A:" + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B:" + i))
                .map(i -> "#" + i * 10)
                .doOnNext(i -> System.out.println("C:" + i))
                .filter(s -> s.length() < 4)
                .subscribe(System.out::println);

    }

    @Test
    void testFlatMapWithLogging() {
        Observable<Integer> numbers = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("Map and Filter");
        numbers
                .doOnNext(i -> System.out.println("Num:" + i))
                .map(x -> x * 2)
                .doOnNext(i -> System.out.println("Num*2:" + i))
                .filter(x -> x % 3 == 0)
                .doOnNext(i -> System.out.println("DivisibleBy3:" + i))
                .subscribe(System.out::println);

        System.out.println("FlatMap Map");
        numbers.flatMap(x -> Observable.just(x * 2)).subscribe(System.out::println);

        System.out.println("FlatMap Filter");
        numbers.flatMap(x -> x % 3 == 0 ? Observable.just(x) : Observable.empty()).subscribe(System.out::println);

    }

    @Test
    void testMorseCode() {
        Observable.just('S', 'P', 'A', 'R', 'T', 'A')
                //.doOnNext(i-> System.out.println("Char:" + i))
                .map(Character::toUpperCase)
                //.doOnNext(i-> System.out.println("ToUpper:" + i))
                .flatMap(MorseCodeUtils::toMorseCode)
                //.doOnNext(i-> System.out.println("ToMorse:" + i))
                .map(Sound::toString).subscribe(System.out::println);
    }

    @Test
    void testDelay() throws InterruptedException {
        Observable.just('x', 'y', 'z').delay(1, TimeUnit.SECONDS).subscribe(System.out::println);
        Thread.sleep(1100);
    }

    @Test
    void testDelay2() throws InterruptedException {

        Observable.just("Lorem", "Ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit")
                .delay(word -> Observable.timer(word.length(), TimeUnit.SECONDS))
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(15);
    }

    @Test
    void testDelayFlatMap() throws InterruptedException {

        Observable.just("Lorem", "Ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit")
                .flatMap(word -> Observable.timer(word.length(), TimeUnit.SECONDS).map(x -> word))
                .subscribe(System.out::println);

        TimeUnit.SECONDS.sleep(15);
    }

    @Test
    void testTimer() throws InterruptedException {
        Observable.timer(1, TimeUnit.SECONDS).flatMap(i -> Observable.just('x', 'y', 'z')).subscribe(System.out::println);
        Thread.sleep(1100);
    }

    @Test
    void testOrderOfEvents() throws InterruptedException {

        Observable.just(10L, 1L).flatMap(x -> Observable.just(x).delay(x, TimeUnit.SECONDS)).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(15);
    }

    @Test
    void testDayOfWeekDelay() throws InterruptedException {
        Observable.just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY).flatMap(OperatorsAndTransformations::loadRecordsFor).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void testDayOfWeekDelayPreserveOrder() throws InterruptedException {
        Observable.just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY).concatMap(OperatorsAndTransformations::loadRecordsFor).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void temperatureZipTests() {

        Observable<Temperature> temperatureObservable = Observable.just(
                Temperature.builder().fahrenheit(60.0f).build(),
                Temperature.builder().fahrenheit(60.2f).build(),
                Temperature.builder().fahrenheit(60.3f).build(),
                Temperature.builder().fahrenheit(61.0f).build()
        );

        Observable<Wind> windObservable = Observable.just(
                Wind.builder().speed(6).build(),
                Wind.builder().speed(6).build(),
                Wind.builder().speed(10).build()

        );

        Observable<Weather> weatherObservable = temperatureObservable.zipWith(windObservable, Weather::build);
        weatherObservable.subscribe(System.out::println);
        weatherObservable.map(Weather::feelsLikeFahrenheit).subscribe(System.out::println);
    }

    @Test
    void cartesianProductTest() {
        Observable<Integer> oneToEight = Observable.range(1, 8).doOnNext(x -> System.out.println("OneToEight:" + x));

        Observable<String> ranks = oneToEight.map(Object::toString).doOnNext(x -> System.out.println("Ranks:" + x));
        ;
        Observable<String> files = oneToEight.map(x -> 'A' + x - 1).map(ascii -> (char) ascii.intValue()).map(x -> Character.toString(x)).doOnNext(x -> System.out.println("Files:" + x));
        ;

        Observable<String> squares = files.flatMap(file -> ranks.map(rank -> file + rank));
        ;

        squares.subscribe(System.out::println);

    }

    @Test
    void vacationTest() {
        Observable<LocalDate> nextTenDays = Observable.range(1, 10).map(i -> LocalDate.now().plusDays(i));

        Observable<Vacation> possibleVacations = Observable.just(City.WARSAW, City.LONDON, City.PARIS)
                .flatMap(city -> nextTenDays.map(date -> new Vacation(city, date)))
                .flatMap(vacation ->
                        Observable.zip(
                                vacation.weather().filter(Weather::isSunny),
                                vacation.flightFrom(City.NEW_YORK),
                                vacation.hotel(),
                                (w, f, h) -> vacation

                        )
                );
    }

    @Test
    void zipWithSynchronized() throws InterruptedException {
        Observable<Long> red = Observable.interval(10, TimeUnit.MILLISECONDS);
        Observable<Long> green = Observable.interval(10, TimeUnit.MILLISECONDS);

        Observable.zip(red.timestamp(), green.timestamp(), (r, g) -> r.getTimestampMillis() - g.getTimestampMillis()).forEach(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    void zipWithNonSynchronized() throws InterruptedException {
        Observable<Long> red = Observable.interval(11, TimeUnit.MILLISECONDS);
        Observable<Long> green = Observable.interval(10, TimeUnit.MILLISECONDS);

        Observable.zip(red.timestamp(), green.timestamp(), (r, g) -> r.getTimestampMillis() - g.getTimestampMillis()).forEach(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    void combineLatestNonSynchronized() throws InterruptedException {
        Observable<Long> red = Observable.interval(11, TimeUnit.MILLISECONDS);
        Observable<Long> green = Observable.interval(10, TimeUnit.MILLISECONDS);

        Observable.combineLatest(red.timestamp(), green.timestamp(), (r, g) -> r.getTimestampMillis() - g.getTimestampMillis()).forEach(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    void combineLatestExample() throws InterruptedException {
        Observable<String> slow = Observable.interval(17, TimeUnit.MILLISECONDS).map(x -> "Slow" + x);
        Observable<String> fast = Observable.interval(10, TimeUnit.MILLISECONDS).map(x -> "Fast" + x);

        Observable.combineLatest(slow, fast, (s, f) -> f + ":" + s).subscribe(System.out::println);

        Thread.sleep(1000);
        /*
        Fast0:Slow0
        Fast0:Slow1
        Fast1:Slow1
         */
    }

    @Test
    void combineLatestFromExample() throws InterruptedException {
        Observable<String> slow = Observable.interval(17, TimeUnit.MILLISECONDS).map(x -> "Slow" + x);
        Observable<String> fast = Observable.interval(10, TimeUnit.MILLISECONDS).map(x -> "Fast" + x);


        slow.withLatestFrom(fast, (s, f) -> s + ":" + f).forEach(System.out::println);

        Thread.sleep(1000);

        /*
        Slow0:Fast0
        Slow1:Fast2
        Slow2:Fast4
        Slow3:Fast4
         */
    }

    @Test
    void combineLatestWithDummyEventsFromExample() throws InterruptedException {
        Observable<String> slow = Observable.interval(17, TimeUnit.MILLISECONDS).map(x -> "Slow" + x);
        Observable<String> fast = Observable.interval(10, TimeUnit.MILLISECONDS).map(x -> "Fast" + x).delay(100, TimeUnit.MILLISECONDS).startWith("Default");


        slow.withLatestFrom(fast, (s, f) -> s + ":" + f).forEach(System.out::println);

        Thread.sleep(1000);

        /*
        Slow0:Default
        Slow1:Default
        Slow2:Default
        Slow3:Default
        Slow4:Default
        Slow5:Default
        Slow6:Default
        Slow7:Fast2
        Slow8:Fast3
        Slow9:Fast5
        Slow10:Fast5
         */
    }

    @Test
    void startWithSimpleExample(){
        Observable.just(1,2).startWith(0).subscribe(System.out::println);
    }

}
