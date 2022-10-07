package rxjava.examples.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rxjava.examples.retrofit.Cities;
import rxjava.examples.retrofit.MeetupApi;

public class CitiesCommand extends HystrixObservableCommand<Cities> {

    private final MeetupApi meetupApi;
    private final double lat;
    private final double lon;

    public CitiesCommand(MeetupApi meetupApi, double lat, double lon) {
        super(HystrixCommandGroupKey.Factory.asKey("Meetup"));
        this.meetupApi = meetupApi;
        this.lat = lat;
        this.lon = lon;
    }


    @Override
    protected Observable<Cities> construct() {
        return meetupApi.listCities(lat, lon);
    }
}
