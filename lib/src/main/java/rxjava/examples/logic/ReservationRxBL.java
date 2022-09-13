package rxjava.examples.logic;

import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.examples.model.Email;
import rxjava.examples.model.Flight;
import rxjava.examples.model.Passenger;
import rxjava.examples.model.Ticket;

public class ReservationRxBL {
    private final ReservationBL reservationBL;

    public ReservationRxBL() {
        reservationBL = new ReservationBL();
    }

    public Observable<Email> reserveTicketSequential(String flightNumber, String passengerId){

        Observable<Flight> flightObservable = lookupFlight(flightNumber);
        Observable<Passenger> passengerObservable = lookupPassenger(passengerId);
        Observable<Ticket> ticketObservable = bookTicket(flightObservable, passengerObservable);
        return sendEmail(ticketObservable);
    }

    public Observable<Email> reserveTicketParallel(String flightNumber, String passengerId){

        Observable<Flight> flightObservable = lookupFlight(flightNumber).subscribeOn(Schedulers.io());
        Observable<Passenger> passengerObservable = lookupPassenger(passengerId).subscribeOn(Schedulers.io());
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
        return ticket.map(reservationBL::sendEmail);
    }
}
