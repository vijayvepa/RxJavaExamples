package rxjava.examples.logic;

import org.jeasy.random.EasyRandom;
import rxjava.examples.model.Tweet;

import java.math.BigInteger;

public class TweetBL {

    private final EasyRandom easyRandom;

    public TweetBL() {
        easyRandom = new EasyRandom();
    }

    public Tweet getTweet(BigInteger next) {
        Tweet tweet = easyRandom.nextObject(Tweet.class);
        tweet.setSequenceId(next);
        return tweet;
    }
}
