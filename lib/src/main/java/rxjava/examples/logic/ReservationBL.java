package rxjava.examples.logic;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.SneakyThrows;
import rxjava.examples.model.Email;
import rxjava.examples.model.Flight;
import rxjava.examples.model.Passenger;
import rxjava.examples.model.Ticket;

import java.time.LocalDateTime;

public class ReservationBL {

    private final EnhancedRandom enhancedRandom;

    public ReservationBL() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    }

    public Email reserveTicket(String flightNumber, String passengerId) {

        Flight flight = lookupFlight(flightNumber);
        Passenger passenger = lookupPassenger(passengerId);
        Ticket ticket = bookTicket(flight, passenger);
        return sendEmail(ticket);
    }

    @SneakyThrows
    Flight lookupFlight(String flightNumber) {
        System.out.println("Looking up flight details...");
        Thread.sleep(3000);

        return new Flight().flightNumber(flightNumber);
    }

    @SneakyThrows
    Passenger lookupPassenger(String passengerId) {
        System.out.println("Looking up passenger...");
        Thread.sleep(3000);
        return enhancedRandom.nextObject(Passenger.class).passengerId(passengerId);

    }

    @SneakyThrows
    Ticket bookTicket(Flight flight, Passenger passenger) {
        System.out.println("Booking ticket...");
        Thread.sleep(1000);
        return new Ticket().flight(flight).passenger(passenger).date(LocalDateTime.now().toString());
    }

    @SneakyThrows
    Email sendEmail(Ticket ticket) {

        System.out.println("Sending email...");
        Thread.sleep(500);
        return new Email().ticket(ticket).emailDate(LocalDateTime.now().toString());
    }
}
