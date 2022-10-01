package rxjava.examples.samples;

import rx.Observable;
import rx.subscriptions.Subscriptions;
import rxjava.examples.logic.TweetBL;
import rxjava.examples.model.Tweet;

import java.math.BigInteger;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactiveExtensions {

    private final TweetBL tweetBL;

    public ReactiveExtensions() {
        tweetBL = new TweetBL();
    }

    public <T> Observable<T> delayed(T x) {
        return Observable.create(subscriber -> {
            Runnable r = () -> {
                sleep(10, TimeUnit.SECONDS);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(x);
                    subscriber.onCompleted();
                }
            };
            Thread t = new Thread(r);
            t.start();
            subscriber.add(Subscriptions.create(t::interrupt));
        });
    }

    Observable<Tweet> loadAll(Collection<Integer> ids) {
        return Observable.create(subscriber -> {

            ExecutorService pool = Executors.newFixedThreadPool(10);

            AtomicInteger countDown = new AtomicInteger(ids.size());

            //Violates Rx contract
            ids.forEach(id -> pool.submit(() -> {
                final Tweet tweet = load(id);
                subscriber.onNext(tweet);

                if (countDown.decrementAndGet() == 0) {
                    pool.shutdownNow();
                    subscriber.onCompleted();
                }
            }));

        });
    }

    private Tweet load(Integer id) {
        return tweetBL.getTweet(BigInteger.valueOf(id));
    }

    @SuppressWarnings("SameParameterValue")
    public void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException ignore) {

        }
    }

    public Observable<Long> timedObservable() {
        return Observable.timer(1, TimeUnit.SECONDS);
    }

    public Observable<Long> intervalBasedObservable() {
        return Observable.interval(1_000_000 / 60, TimeUnit.MICROSECONDS);
    }

    public void scheduleAtFixedRate(Runnable command) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(command, 0, 10, TimeUnit.MILLISECONDS);
    }
}
