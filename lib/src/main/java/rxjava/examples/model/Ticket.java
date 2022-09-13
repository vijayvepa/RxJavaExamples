package rxjava.examples.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Ticket {
    private Flight flight;
    private Passenger passenger;
    private String date;
}
