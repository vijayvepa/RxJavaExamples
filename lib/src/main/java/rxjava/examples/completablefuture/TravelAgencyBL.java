package rxjava.examples.completablefuture;

import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;
import rxjava.examples.model.Flight;
import rxjava.examples.model.Geolocation;
import rxjava.examples.model.Ticket;
import rxjava.examples.model.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TravelAgencyBL {

    private final UserBL userBL;
    private final GeolocationBL geolocationBL;
    private final FlightBL flightBL;

    public TravelAgencyBL(UserBL userBL, GeolocationBL geolocationBL, FlightBL flightBL) {
        this.userBL = userBL;
        this.geolocationBL = geolocationBL;
        this.flightBL = flightBL;
    }

    //region thread pool
    Ticket bookFastest(List<TravelAgency> travelAgencies) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(10);


        String id = UUID.randomUUID().toString();
        User user = userBL.findById(id);
        final Geolocation location = geolocationBL.locate();

        ExecutorCompletionService<Flight> ecs = new ExecutorCompletionService<>(pool);

        travelAgencies.forEach(travelAgency ->
                ecs.submit(() -> travelAgency.search(user, location)));

        Future<Flight> firstFlight = ecs.poll(5, TimeUnit.SECONDS);
        Flight flight = firstFlight.get();
        return flightBL.book(flight);

    }

    //endregion

    //region completable future
    CompletableFuture<Ticket> bookFastestAsync(List<TravelAgency> travelAgencies) {


        String id = UUID.randomUUID().toString();
        final CompletableFuture<User> user = userBL.findByIdAsync(id);


        final CompletableFuture<Geolocation> location = geolocationBL.locateAsync();

        //map -> thenApply
        //flatMap -> thenCompose

        return user.thenCombine(location,
                        (u, l) -> travelAgencies.stream()
                                .map(t -> t.searchAsync(u, l))
                                .reduce((f1, f2) -> f1.applyToEither(f2, Function.identity()))
                                .orElse(null))
                .thenCompose(Function.identity())
                .thenCompose(flightBL::bookAsync);


    }

    CompletableFuture<Ticket> bookFastestAsyncWithAnyOf(List<TravelAgency> travelAgencies) {


        String id = UUID.randomUUID().toString();
        final CompletableFuture<User> user = userBL.findByIdAsync(id);


        final CompletableFuture<Geolocation> location = geolocationBL.locateAsync();


        return user.thenCombine(location,
                        (u, l) -> getAnyFlightCompletableFuture(travelAgencies, u, l))
                .thenCompose(Function.identity())
                .thenCompose(flightBL::bookAsync);


    }

    private static CompletableFuture<Flight> getAnyFlightCompletableFuture(List<TravelAgency> travelAgencies, User u, Geolocation l) {

        final List<CompletableFuture<Flight>> flightFutures = travelAgencies.stream().map(travelAgency -> travelAgency.searchAsync(u, l)).collect(Collectors.toList());
        CompletableFuture<?>[] futuresArray = new CompletableFuture<?>[flightFutures.size()];
        flightFutures.toArray(futuresArray);

        return CompletableFuture.anyOf(futuresArray).thenApply(x -> ((Flight) x));
    }

    CompletableFuture<ZonedDateTime> getZonedDateTimeAsync() {

        CompletableFuture<Long> timeFuture = CompletableFuture.supplyAsync(System::currentTimeMillis);
        CompletableFuture<ZoneId> zoneIdCompletableFuture = CompletableFuture.supplyAsync(() -> ZoneId.of("America/New_York"));

        CompletableFuture<Instant> instantCompletableFuture = timeFuture.thenApply(Instant::ofEpochMilli);

        return instantCompletableFuture.thenCombine(zoneIdCompletableFuture, ZonedDateTime::ofInstant);
    }
    //endregion

    //observable

    public Observable<Ticket> bookFastestReactive(List<TravelAgency> travelAgencies) {
        String id = UUID.randomUUID().toString();
        final Observable<User> user = userBL.findByIdReactive(id);
        final Observable<Geolocation> location = geolocationBL.locateReactive();
        final Observable<TravelAgency> agencies = Observable.from(travelAgencies);

        return user.zipWith(location, (u, l) ->
                        agencies
                                .flatMap(a -> a.searchObservable(u, l))
                                .first()
                )
                .flatMap(x -> x)
                .flatMap(flightBL::bookReactive);
    }

    public Observable<Ticket> bookFastestReactiveAlt(List<TravelAgency> travelAgencies) {
        String id = UUID.randomUUID().toString();
        final Observable<User> user = userBL.findByIdReactive(id);
        final Observable<Geolocation> location = geolocationBL.locateReactive();
        final Observable<TravelAgency> agencies = Observable.from(travelAgencies);

        return user
                .zipWith(location, Pair::of)
                .flatMap(pair ->
                        agencies.flatMap(agency ->
                                agency.searchObservable(pair.getLeft(), pair.getRight()))
                )
                .first()
                .flatMap(flightBL::bookReactive);
    }

    //endregion

}
