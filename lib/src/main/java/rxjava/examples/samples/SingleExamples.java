package rxjava.examples.samples;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.springframework.jdbc.core.JdbcTemplate;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class SingleExamples {

    private final AsyncHttpClient asyncHttpClient;
    private final JdbcTemplate jdbcTemplate;

    public SingleExamples() {
        asyncHttpClient = new AsyncHttpClient();
        jdbcTemplate = new JdbcTemplate();
    }

    //region simple
    public void singleExample() {
        final Single<String> helloWorldSingle = Single.just("Hello World!");
        helloWorldSingle.subscribe(System.out::println);

    }

    public void errorExample() {
        final Single<Instant> error = Single.error(new RuntimeException("oops!"));

        error.observeOn(Schedulers.io())
                .subscribe(System.out::println,
                        Throwable::printStackTrace);

    }
    //endregion

    //region fetch
    public Single<Response> fetch(String address) {

        return Single.create(singleSubscriber ->
                asyncHttpClient.prepareGet(address)
                        .execute(handler(singleSubscriber))
        );
    }

    private AsyncCompletionHandler<Response> handler(SingleSubscriber<? super Response> singleSubscriber) {
        return new AsyncCompletionHandler<>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                singleSubscriber.onSuccess(response);
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                singleSubscriber.onError(t);
            }
        };
    }

    public Single<String> body(Response response) {
        return Single.create(singleSubscriber -> {
            try {
                singleSubscriber.onSuccess(response.getResponseBody(StandardCharsets.UTF_8.displayName()));
            } catch (IOException e) {
                singleSubscriber.onError(e);
            }
        });
    }

    public Single<String> bodyCallable(Response response) {
        return Single.fromCallable(() -> response.getResponseBody(StandardCharsets.UTF_8.displayName()));
    }
    //endregion

    //region zip
    public Single<String> content(int id) {
        return Single.fromCallable(() ->
                        jdbcTemplate.queryForObject(
                                "SELECT * FROM articles WHERE id=?", String.class, id))
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> likes(int id) {
        return fetch("https://facebook.com/likes?contentId=" + id)
                .subscribeOn(Schedulers.io()).flatMap(this::bodyCallable).map(Integer::parseInt);
    }

    public Single<Integer> updateReadCount() {
        return Single.fromCallable(() -> jdbcTemplate.update("UPDATE articles SET readCount = readCount + 1")).subscribeOn(Schedulers.io());
    }

    public Single<String> htmlFor(int id) {
        return Single.zip(
                content(id),
                likes(id),
                updateReadCount(),
                (c, l, u) -> buildHtml(c, l)
        );
    }


    String buildHtml(String content, int likes) {
        return "<html>" + content + "</html>";
    }
    //endregion

    //region caching
    public Single<String> simpleSingle42() {
        return Single.create(singleSubscriber -> {
            System.out.println("Subscribing");
            singleSubscriber.onSuccess("42");
        });

    }

    public Single<String> cachedSingle() {
        return simpleSingle42().toObservable().cache().toSingle();
    }

    //endregion
}
