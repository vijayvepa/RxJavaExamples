package rxjava.examples.completablefuture;

import rxjava.examples.model.Geolocation;

import java.util.concurrent.CompletableFuture;

public class GeolocationBL {
    public Geolocation locate() {
        return new Geolocation();
    }

    public CompletableFuture<Geolocation> locateAsync() {
        return CompletableFuture.supplyAsync(this::locate);
    }
}
