package rxjava.examples.completablefuture;

import rx.Observable;
import rxjava.examples.ObservableUtils;
import rxjava.examples.model.Geolocation;

import java.util.concurrent.CompletableFuture;

public class GeolocationBL {
    public Geolocation locate() {
        return new Geolocation();
    }

    public CompletableFuture<Geolocation> locateAsync() {
        return CompletableFuture.supplyAsync(this::locate);
    }

    public Observable<Geolocation> locateReactive() {
        return ObservableUtils.fromCompletableFuture(locateAsync());
    }
}
