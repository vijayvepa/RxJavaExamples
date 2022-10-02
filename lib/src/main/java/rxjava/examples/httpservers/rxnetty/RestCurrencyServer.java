package rxjava.examples.httpservers.rxnetty;

import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import java.math.BigDecimal;

public class RestCurrencyServer {
    public static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(String[] args) {
        HttpServer.newServer(8080)
                .start((req, res) -> {

                    String amountStr = req.getDecodedPath().substring(1);
                    Observable<String> response = getEurJson(amountStr);

                    res.setHeader("Content-Type", "application/json");
                    return res.writeString(response);
                })
                .awaitShutdown();
    }

    private static Observable<String> getEurJson(String amountStr) {

        BigDecimal amount = new BigDecimal(amountStr);

        return Observable.just(amount).map(RestCurrencyServer::getUsd).map(usd -> getJson(amount, usd));
    }

    private static String getJson(BigDecimal amount, BigDecimal usd) {
        return String.format(
                "{\"EUR\": %f, \"USD\": %f }",
                amount, usd);

    }

    private static BigDecimal getUsd(BigDecimal eur) {
        return eur.multiply(RATE);
    }
}
