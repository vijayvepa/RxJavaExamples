package rxjava.examples.completablefuture;

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

    CompletableFuture<Ticket> bookFastestAsync(List<TravelAgency> travelAgencies) throws InterruptedException, ExecutionException {


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

    CompletableFuture<Ticket> bookFastestAsyncWithAnyOf(List<TravelAgency> travelAgencies) throws InterruptedException, ExecutionException {


        String id = UUID.randomUUID().toString();
        final CompletableFuture<User> user = userBL.findByIdAsync(id);


        final CompletableFuture<Geolocation> location = geolocationBL.locateAsync();


        return user.thenCombine(location,
                        (u, l) -> getFlightCompletableFuture(travelAgencies, u, l))
                .thenCompose(Function.identity())
                .thenCompose(flightBL::bookAsync);


    }

    private static CompletableFuture<Flight> getFlightCompletableFuture(List<TravelAgency> travelAgencies, User u, Geolocation l) {

        final List<CompletableFuture<Flight>> flightFutures = travelAgencies.stream().map(travelAgency -> travelAgency.searchAsync(u, l)).collect(Collectors.toList());
        CompletableFuture<Flight>[] futuresArray = new CompletableFuture<>[flightFutures.size()];
        flightFutures.toArray(futuresArray);

        return CompletableFuture.anyOf(futuresArray).thenApply(x -> ((Flight) x));
    }

    CompletableFuture<ZonedDateTime> getZonedDateTimeAsync() {

        CompletableFuture<Long> timeFuture = CompletableFuture.supplyAsync(System::currentTimeMillis);
        CompletableFuture<ZoneId> zoneIdCompletableFuture = CompletableFuture.supplyAsync(() -> ZoneId.of("America/New_York"));

        CompletableFuture<Instant> instantCompletableFuture = timeFuture.thenApply(Instant::ofEpochMilli);

        return instantCompletableFuture.thenCombine(zoneIdCompletableFuture, ZonedDateTime::ofInstant);
    }
}
