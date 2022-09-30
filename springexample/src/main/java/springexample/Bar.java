package springexample;

import org.springframework.stereotype.Component;
import rx.Observable;
import twitter4j.Status;

import java.time.Instant;
import java.util.Date;

@Component
public class Bar {

    private final Log log;
    private final Observable<Status> tweets;

    public Bar(Observable<Status> tweets, Log log){
        this.log = log;
        this.tweets = tweets;

    }

    public void start(){
        tweets.subscribe(status -> log.info(status.getText()));
        log.info("Subscribed");
    }

    public Observable<Status> retweets(){
        return tweets.filter(Status::isRetweet);
    }

    public void mappedTweets(){
        Observable<String> tweetTexts = tweets.map(Status::getText);
        Observable<String> references = tweetTexts.filter(s -> s.startsWith("#"));
        Observable<String>  mentions = tweetTexts.filter(s->s.contains("@"));

        Observable<Date> createdDates = tweets.map(Status::getCreatedAt);
        Observable<Instant> instants = createdDates.map(Date::toInstant);
    }
}

