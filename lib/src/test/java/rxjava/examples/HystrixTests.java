package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.examples.hystrix.BlockingCommand;
import rxjava.examples.hystrix.CitiesCommand;
import rxjava.examples.logic.BookBL;
import rxjava.examples.logic.RatingBL;
import rxjava.examples.retrofit.Cities;
import rxjava.examples.retrofit.MeetupApi;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class HystrixTests {

    @Test
    void executeTest() {
        String string = new BlockingCommand().execute();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void queueTest() throws ExecutionException, InterruptedException {
        String string = new BlockingCommand().queue().get();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void observeTest() {
        final String string = new BlockingCommand().observe().toBlocking().single();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void observableTest() {
        final String string = new BlockingCommand().toObservable().toBlocking().single();
        Log.log(string);
        assertNotNull(string);
    }

    @Test
    void observableWithOperationsTest() {
        new BlockingCommand().toObservable().doOnError(ex -> Log.log("WARN:" + ex))
                .retryWhen(ex -> ex.delay(500, TimeUnit.MILLISECONDS))
                .timeout(3, TimeUnit.SECONDS)
                .toBlocking()
                .subscribe(Log::log);

    }

    @Test
    void hystrixCircuitTest() {
        MeetupApi meetupApi = mock(MeetupApi.class);

        given(meetupApi.listCities(anyDouble(), anyDouble())).willReturn(
                Observable.<Cities>error(new RuntimeException("Broken"))
                        .doOnSubscribe(() -> Log.log("DEBUG: Invoking"))
                        .delay(2, TimeUnit.SECONDS)
        );

        Observable.interval(50, TimeUnit.MILLISECONDS)
                .doOnNext(x -> Log.log("DEBUG: Requesting"))
                .flatMap(x -> new CitiesCommand(meetupApi, 52.229841, 21.011736).toObservable()
                        .doOnError(t -> Log.log("ERROR: " + t))
                        .onErrorResumeNext(ex -> Observable.empty()))
                .toBlocking().subscribe(Log::log);

    }

    @Test
    void ratingTest() {
        RatingBL ratingBL = new RatingBL();
        new BookBL().allBooks().take(5).flatMap(ratingBL::fetchRating).toBlocking().subscribe(Log::log);
    }
}
