package rxjava.examples.httpclients;


import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;
import rxjava.examples.Log;

import java.nio.charset.StandardCharsets;

public class RxNettyHttpClient {
    public static void main(String[] args) {
        final Observable<ByteBuf> response = HttpClient.newClient("google.com", 80).createGet("/")
                .flatMap(HttpClientResponse::getContent);

        response.map(bb ->
                        bb.toString(StandardCharsets.UTF_8)).toBlocking()
                .subscribe(Log::log);

    }
}
