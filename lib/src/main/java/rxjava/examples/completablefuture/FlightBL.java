package rxjava.examples.completablefuture;

import rx.Observable;
import rxjava.examples.model.Flight;
import rxjava.examples.model.Ticket;
import rxjava.examples.utils.ObservableUtils;

import java.util.concurrent.CompletableFuture;

public class FlightBL {
    Ticket book(Flight flight) {
        return new Ticket();
    }

    CompletableFuture<Ticket> bookAsync(Flight flight) {
        return CompletableFuture.supplyAsync(() -> book(flight));
    }

    Observable<Ticket> bookReactive(Flight flight) {
        return ObservableUtils.fromCompletableFuture(bookAsync(flight));
    }
}
