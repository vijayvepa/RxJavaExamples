package rxjava.examples.httpservers.rxnetty;

import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

public class RxNettyHttpServer {
    private static final Observable<String> RESPONSE_OK = Observable.just("OK");

    public static void main(String[] args) {
        HttpServer.newServer(8080)
                .start((req, resp) ->
                        resp
                                .setHeader(CONTENT_LENGTH, 2)
                                .writeStringAndFlushOnEach(RESPONSE_OK)
                )
                .awaitShutdown();
    }
}
