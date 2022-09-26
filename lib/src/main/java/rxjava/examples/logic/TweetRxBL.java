package rxjava.examples.logic;

import org.jeasy.random.EasyRandom;
import rx.Emitter;
import rx.Observable;
import rxjava.examples.model.Tweet;

import java.math.BigInteger;
import java.util.Random;

public class TweetRxBL {

    private final NaturalNumbersIterator naturalNumbersIterator;
    private final EasyRandom easyRandom;
    private final Random random;

    public TweetRxBL() {
        this.easyRandom = new EasyRandom();
        naturalNumbersIterator = new NaturalNumbersIterator();
        random = new Random();
    }

    public Observable<Tweet> tweetStream(){
        return Observable.create(s->{
            while (naturalNumbersIterator.hasNext()){
                BigInteger next = naturalNumbersIterator.next();

                s.onNext(getTweet(next));
                try {
                    sleepRandom();
                } catch (InterruptedException e) {
                    s.onError(e);
                }
            }
        }, Emitter.BackpressureMode.NONE);
    }

    private void sleepRandom() throws InterruptedException {
        int rand = random.nextInt(100);

            Thread.sleep(rand);
    }

    private Tweet getTweet(BigInteger next) {
        Tweet tweet = easyRandom.nextObject(Tweet.class);
        tweet.setSequenceId(next);
        return tweet;
    }


}
