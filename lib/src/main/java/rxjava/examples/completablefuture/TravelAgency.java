package rxjava.examples.completablefuture;

import rxjava.examples.model.Flight;
import rxjava.examples.model.Geolocation;
import rxjava.examples.model.User;

import java.util.concurrent.CompletableFuture;

public interface TravelAgency {
    Flight search(User user, Geolocation geolocation);

    CompletableFuture<Flight> searchAsync(User user, Geolocation geolocation);
}
