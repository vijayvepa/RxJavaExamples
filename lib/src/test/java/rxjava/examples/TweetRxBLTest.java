package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rxjava.examples.logic.TweetRxBL;
import rxjava.examples.model.Tweet;

public class TweetRxBLTest {
    private final TweetRxBL tweetRxBL = new TweetRxBL();

    @Test
    void tweetSubscribeTest() throws InterruptedException {
        Observable<Tweet> tweetObservable = tweetRxBL.tweetStream().subscribeOn(Schedulers.io());;

        tweetObservable.subscribe(System.out::println, Throwable::printStackTrace, ()->System.out.println("complete"));
        Thread.sleep(10000);
    }

    @Test

    void tweetObserverTest() throws InterruptedException {
        Observable<Tweet> tweetObservable = tweetRxBL.tweetStream().subscribeOn(Schedulers.io());;

        Observer<Tweet> observer = new Observer<>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Tweet tweet) {
                System.out.println("OBSERVER:" + tweet);
            }
        };

        tweetObservable.subscribe(observer);

        Thread.sleep(10000);
    }


    @Test
    void tweetSubscriberTest() throws InterruptedException {
        Observable<Tweet> tweetObservable = tweetRxBL.tweetStream().subscribeOn(Schedulers.io());

        Subscriber<Tweet> observer = new Subscriber<>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Tweet tweet) {

                if(tweet.getSequenceId().intValue() > 100){
                    unsubscribe();
                }

                System.out.println("SUBSCRIBER: " + tweet);
            }
        };

        tweetObservable.subscribe(observer);
        Thread.sleep(10000);

    }

    @Test
    void tweetSubscriberWithUnsubscribeTest() throws InterruptedException {
        Observable<Tweet> tweetObservable = tweetRxBL.tweetStreamWithUnsubscribe();

        Subscriber<Tweet> observer = new Subscriber<>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Tweet tweet) {

                if(tweet.getSequenceId().intValue() > 100){
                    unsubscribe();
                }

                System.out.println("SUBSCRIBER: " + tweet);
            }
        };

        tweetObservable.subscribe(observer);
        Thread.sleep(10000);

    }


}
