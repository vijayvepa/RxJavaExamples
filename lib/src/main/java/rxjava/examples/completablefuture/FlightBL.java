package rxjava.examples.completablefuture;

import rxjava.examples.model.Flight;
import rxjava.examples.model.Ticket;

import java.util.concurrent.CompletableFuture;

public class FlightBL {
    Ticket book(Flight flight) {
        return new Ticket();
    }

    CompletableFuture<Ticket> bookAsync(Flight flight) {
        return CompletableFuture.supplyAsync(() -> book(flight));
    }
}
