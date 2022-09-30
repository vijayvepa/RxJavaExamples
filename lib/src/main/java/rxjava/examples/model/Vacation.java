package rxjava.examples.model;

import rx.Observable;

import java.time.LocalDate;

public class Vacation {
    private final City where;
    private final LocalDate when;


    public Vacation(City where, LocalDate when) {
        this.where = where;
        this.when = when;
    }

    public Observable<Weather> weather(){
        return Observable.empty();
    }

    public Observable<Flight> flightFrom(City city)  {
        return Observable.empty();
    }

    public Observable<Hotel> hotel()  {
        return Observable.empty();
    }

}
