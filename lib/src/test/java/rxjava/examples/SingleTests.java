package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Single;
import rxjava.examples.samples.SingleExamples;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SingleTests {

    SingleExamples singleExamples = new SingleExamples();

    @Test
    void singleExampleTest() {
        singleExamples.singleExample();
    }

    @Test
    void singleExampleErrorTest() {
        singleExamples.errorExample();
    }

    @Test
    void asyncHttpClientTest() {
        final Single<String> responseBody = singleExamples.fetch("https://www.google.com").flatMap(singleExamples::body);

        final String body = responseBody.toBlocking().value();

        System.out.println(body);
        assertNotEquals("", body);
    }

    @Test
    void asyncHttpClientCallableTest() {
        final Single<String> responseBody = singleExamples.fetch("https://www.google.com").flatMap(singleExamples::bodyCallable);

        final String body = responseBody.toBlocking().value();
        System.out.println(body);
        assertNotEquals("", body);
    }

    @Test
    void nonCachingTest() {

        final Single<String> nonCachedSingle = singleExamples.simpleSingle42();
        nonCachedSingle.subscribe(System.out::println);
        nonCachedSingle.subscribe(System.out::println);

    }

    @Test
    void cachingTest() {

        final Single<String> cachedSingle = singleExamples.cachedSingle();
        cachedSingle.subscribe(System.out::println);
        cachedSingle.subscribe(System.out::println);

    }
}
