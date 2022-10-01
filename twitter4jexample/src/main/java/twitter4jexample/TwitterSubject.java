package twitter4jexample;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;
import twitter4j.*;

public class TwitterSubject {

    public static void main(String[] args) {
        TwitterSubject twitterSubject = new TwitterSubject();
        Observable<Status> tweets = twitterSubject.observe();

        tweets.doOnNext(twitterSubject::saveStatus);

        ConnectableObservable<Status> publish = tweets.publish();
        publish.connect();
    }

    private void saveStatus(Status status) {
        System.out.println("Save to db " + status);
    }

    private final PublishSubject<Status> subject = PublishSubject.create();

    TwitterSubject() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                subject.onNext(status);
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

                subject.onError(ex);
            }
        });

        twitterStream.sample();
    }

    public Observable<Status> observe() {
        return subject;
    }
}
