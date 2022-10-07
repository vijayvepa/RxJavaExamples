package rxjava.examples.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.schedulers.Schedulers;
import rxjava.examples.Log;

import java.util.Objects;


public interface GeoNames {

    @GET("/searchJSON")
    Observable<SearchResult> search(
            @Query("q") String query,
            @Query("maxRows") int maxRows,
            @Query("style") String style,
            @Query("username") String username
    );

    default Observable<SearchResult> search(String query) {
        return search(query, 1, "LONG", "some_user");
    }

    default Observable<Integer> populationOf(String query) {

        return search(query)
                .concatMapIterable(SearchResult::getGeonames)
                .map(GeoName::getPopulation)
                .first(Objects::nonNull)
                .singleOrDefault(0)
                .doOnError(th -> Log.log("WARN: Falling back to 0 for " + query + ", " + th))
                .onErrorReturn(throwable -> 0)
                .subscribeOn(Schedulers.io());

    }
}
