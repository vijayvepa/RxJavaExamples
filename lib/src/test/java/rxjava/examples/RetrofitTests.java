package rxjava.examples;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rxjava.examples.retrofit.Cities;
import rxjava.examples.retrofit.City;
import rxjava.examples.retrofit.GeoNames;
import rxjava.examples.retrofit.MeetupApi;

public class RetrofitTests {

    public void retrofitTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit meetupRetrofit = getRetrofit(objectMapper, "https://api.meetup.com");

        MeetupApi meetupApi = meetupRetrofit.create(MeetupApi.class);


        double warsawLat = 52.229841;
        double warsawLon = 21.011736;

        Observable<Cities> cities = meetupApi.listCities(warsawLat, warsawLon);
        final Observable<City> cityList = cities.concatMapIterable(Cities::getResults);

        Observable<String> cityName = cityList.filter(c -> c.distanceTo(warsawLat, warsawLon) < 50)
                .map(City::getCity);

        Retrofit geoNamesRetrofit = getRetrofit(objectMapper, "http://api.geonames.org");
        GeoNames geoNames = geoNamesRetrofit.create(GeoNames.class);

        Observable<Long> totalPopulation = cityName.flatMap(geoNames::populationOf)
                .reduce(0L, Long::sum);

    }

    private static Retrofit getRetrofit(ObjectMapper objectMapper, String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }
}
