package twitter4jexample;

import rx.Observable;
import rx.subscriptions.Subscriptions;
import twitter4j.Status;
import twitter4j.TwitterStream;

public class TwitterStreamRxExample {
    private final TwitterStreamExample twitterStreamExample;

    public static void main(String[] args) {
        TwitterStreamRxExample twitterStreamRxExample = new TwitterStreamRxExample();

        twitterStreamRxExample.twitterStreamObservation().subscribe(
                status -> System.out.println("Status:" + status),
                ex -> System.err.println("Error:" + ex)
        );
    }

    public TwitterStreamRxExample() {
        twitterStreamExample = new TwitterStreamExample();
    }

    Observable<Status> twitterStreamObservation() {
        return Observable.create(subscriber -> {
            TwitterStream twitterStream = twitterStreamExample.consume(
                    (status, stream) -> {
                        if (subscriber.isUnsubscribed()) {
                            stream.shutdown();
                        } else {
                            subscriber.onNext(status);
                        }
                    },
                    (ex, stream) -> {
                        if (subscriber.isUnsubscribed()) {
                            stream.shutdown();
                        } else {
                            subscriber.onError(ex);
                        }
                    });

            subscriber.add(Subscriptions.create(twitterStream::shutdown));
        });
    }
}
