package twitter4jexample;

import twitter4j.*;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class TwitterStreamExample {

    public static void main(String[] args) throws InterruptedException {
        TwitterStreamExample twitterStreamExample = new TwitterStreamExample();
        twitterStreamExample.start();
        twitterStreamExample.consume((status, stream) -> System.out.println("Status: " + status),
                (ex,stream) -> System.err.println("Error:" + ex));

    }

    public void start() throws InterruptedException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("Status: " + status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                System.out.println("Deletion: " + statusDeletionNotice);
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                System.out.println("Track limitation:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

                System.out.println("Scrub Geo: " + userId + "," + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Stall " + warning);

            }

            @Override
            public void onException(Exception ex) {
                System.err.println("Exception" + ex);
            }
        });

        twitterStream.sample();
        TimeUnit.SECONDS.sleep(10);
        twitterStream.shutdown();
    }

    TwitterStream consume(BiConsumer<Status, TwitterStream> onStatus, BiConsumer<Exception, TwitterStream> onException){
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                onStatus.accept(status, twitterStream);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

            @Override
            public void onException(Exception ex) {
                onException.accept(ex, twitterStream);
            }
        });
        return twitterStream;
    }
}
