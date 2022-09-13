package rxjava.examples.logic;

import org.apache.commons.lang3.tuple.Pair;
import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.examples.model.Email;
import rxjava.examples.model.Flight;
import rxjava.examples.model.Passenger;
import rxjava.examples.model.Ticket;

public class ReservationRxBL {
    private final ReservationBL reservationBL;
    private final EmailBL emailBL;

    public ReservationRxBL() {
        reservationBL = new ReservationBL();
        emailBL = new EmailBL();
    }

    public Observable<Email> reserveTicketSequential(String flightNumber, String passengerId){

        Observable<Flight> flightObservable = lookupFlight(flightNumber);
        Observable<Passenger> passengerObservable = lookupPassenger(passengerId);
        Observable<Ticket> ticketObservable = bookTicket(flightObservable, passengerObservable);
        return sendEmail(ticketObservable);
    }

    Observable<Flight> lookupFlight(String flightNumber){
        return Observable.defer(()->
                Observable.just(reservationBL.lookupFlight(flightNumber)));
    }

    Observable<Passenger> lookupPassenger(String passengerId){
        return Observable.defer(()->
                Observable.just(reservationBL.lookupPassenger(passengerId))
        );
    }

    Observable<Ticket> bookTicket(Observable<Flight> flight, Observable<Passenger> passenger){
        return flight.zipWith(passenger, reservationBL::bookTicket);
    }

    Observable<Email> sendEmail(Observable<Ticket> ticket){
        return ticket.map(emailBL::sendEmail);
    }

    Observable<Ticket> bookTicketSquared(Observable<Flight> flight, Observable<Passenger> passenger){
        return flight.zipWith(passenger, Pair::of).flatMap(pair-> bookTicketFrom(pair.getLeft(),pair.getRight()));
    }

    Observable<Ticket> bookTicketFrom(Flight flight, Passenger passenger){
        return Observable.defer(()->
                Observable.just(reservationBL.bookTicket(flight, passenger)));
    }

    public Observable<Email> reserveTicketParallel(String flightNumber, String passengerId){

        Observable<Flight> flightObservable = lookupFlight(flightNumber).subscribeOn(Schedulers.io());
        Observable<Passenger> passengerObservable = lookupPassenger(passengerId).subscribeOn(Schedulers.io());
        Observable<Ticket> ticketObservable = bookTicket(flightObservable, passengerObservable);
        return sendEmail(ticketObservable);
    }

    public Observable<Email> reserveTicketSquared(String flightNumber, String passengerId){

        Observable<Flight> flightObservable = lookupFlight(flightNumber).subscribeOn(Schedulers.io());
        Observable<Passenger> passengerObservable = lookupPassenger(passengerId).subscribeOn(Schedulers.io());
        Observable<Ticket> ticketObservable = bookTicketSquared(flightObservable, passengerObservable);
        return sendEmail(ticketObservable);
    }
}
