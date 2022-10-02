package rxjava.examples.httpservers.rxnetty;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import rx.Observable;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class EurUsdCurrencyTcpServer {
    private static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(String[] args) {
        TcpServer.newServer(8080)
                .<String, String>pipelineConfigurator(pipeline -> {
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
                    pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
                })
                .start(connection -> {
                    Observable<String> output = connection.getInput()
                            .map(BigDecimal::new)
                            .flatMap(EurUsdCurrencyTcpServer::eurToUsd);

                    return connection.writeAndFlushOnEach(output);
                })
                .awaitShutdown();
    }

    private static Observable<String> eurToUsd(BigDecimal eur) {
        return Observable.just(eur.multiply(RATE)).map(amount -> eur + "EUR is " + amount + " USD\r\n")
                .delay(1, TimeUnit.SECONDS);
    }
}
