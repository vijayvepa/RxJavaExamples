package rxjava.examples.logic;

import rx.Emitter;
import rx.Observable;
import rxjava.examples.model.Tweet;

import java.math.BigInteger;
import java.util.Random;

public class TweetRxBL {

    private final NaturalNumbersIterator naturalNumbersIterator;

    private final TweetBL tweetBL;
    private final Random random;

    public TweetRxBL() {
        naturalNumbersIterator = new NaturalNumbersIterator();
        random = new Random();
        tweetBL = new TweetBL();
    }

    public Observable<Tweet> tweetStream(){
        return Observable.create(s->{
            while (naturalNumbersIterator.hasNext()){
                BigInteger next = naturalNumbersIterator.next();

                s.onNext(tweetBL.getTweet(next));
                try {
                    sleepRandom();
                } catch (InterruptedException e) {
                    s.onError(e);
                }
            }
        }, Emitter.BackpressureMode.NONE);
    }

    public Observable<Tweet> tweetStreamWithUnsubscribe(){
        return Observable.create(s->{

            Runnable r = () -> {
                while (naturalNumbersIterator.hasNext()) {

                    if (s.isUnsubscribed()) {
                        break;
                    }

                    BigInteger next = naturalNumbersIterator.next();

                    s.onNext(tweetBL.getTweet(next));
                    try {
                        sleepRandom();
                    } catch (InterruptedException e) {
                        s.onError(e);
                    }
                }
            };
            new Thread(r).start();

        });
    }

    private void sleepRandom() throws InterruptedException {
        int rand = random.nextInt(100);

            Thread.sleep(rand);
    }


    public Observable<Tweet> load(BigInteger id){
        return Observable.create(subscriber -> {
            try{
                subscriber.onNext(tweetBL.getTweet(id));
                subscriber.onCompleted();
            }catch (Exception ex){
                subscriber.onError(ex);
            }
        });
    }


    public Observable<Tweet> callable(BigInteger id){
        return Observable.fromCallable(()-> tweetBL.getTweet(id));
    }

}
