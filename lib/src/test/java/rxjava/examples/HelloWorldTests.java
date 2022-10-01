package rxjava.examples;

import org.junit.jupiter.api.Test;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTests {

    @Test
    public void synchronousObservableCreate() {
        Observable.create(s -> {
                    s.onNext("Hello World");
                    s.onCompleted();
                }
        ).subscribe(System.out::println);
    }

    @Test
    public void synchronousComputation() {
        Observable<Object> objectObservable = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        });

        objectObservable.map(i -> "Number" + i).subscribe(System.out::println);
    }

    @Test
    public void synchronousAndAsynchronousComputation() throws InterruptedException {
        Observable<Integer> objectObservable = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        });

        objectObservable.subscribeOn(Schedulers.io()).doOnNext(i -> System.out.println(Thread.currentThread()))
                .filter(i -> (i % 2) == 0)
                .map(i -> "Value " + i + " processed on " + Thread.currentThread())
                .subscribe(s -> System.out.println("SOME VALUE -> " + s));

        System.out.println("Will print before values are emitted");
        Thread.sleep(100);
    }

    @Test
    public void multipleThreadsMerged() throws InterruptedException {
        Observable<Integer> firstObservable = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(1);
                s.onNext(2);
                s.onNext(3);
                s.onCompleted();
            }).start();
        });

        Observable<Integer> secondObservable = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(4);
                s.onNext(5);
                s.onNext(6);
                s.onCompleted();
            }).start();
        });


        Observable<Integer> merge = Observable.merge(firstObservable, secondObservable);


        merge.doOnNext(i -> System.out.println(Thread.currentThread()))
                .map(i -> "Value " + i + " processed on " + Thread.currentThread())
                .subscribe(s -> System.out.println("Merge Subscribe -> " + s));

        System.out.println("Will print before values are emitted");
        Thread.sleep(100);
    }

    @Test
    public void iterableVsObservable() {
        List<String> items = IntStream.range(0, 75).mapToObj(i -> "Item" + i).collect(Collectors.toList());

        items.stream().skip(10).limit(5).map(s -> s + "_transformed").forEach(System.out::println);

        Observable<String> observableItems = Observable.from(items);

        observableItems.skip(10).take(5).map(s -> s + "_transformedObs").subscribe(System.out::println);
    }

    @Test
    public void mouseEventsFutureVsObservable() throws ExecutionException, InterruptedException {
        Future<String> stringFuture = CompletableFuture.supplyAsync(() -> {
            sleepIgnore(100);
            return "Got it";
        });

        String value = stringFuture.get();
        System.out.println(value);
    }

    @Test
    public void listOfFriendsFutureVsObservable() throws ExecutionException, InterruptedException {
        Future<List<String>> stringFuture = CompletableFuture.supplyAsync(() -> {
            sleepIgnore(100);
            return IntStream.range(0, 75).mapToObj(i -> "Item" + i).collect(Collectors.toList());
        });

        List<String> value = stringFuture.get();
        System.out.println(String.join(",", value));

        Observable<String> observable = Observable.from(IntStream.range(0, 75).mapToObj(i -> "Item" + i).collect(Collectors.toList()));
        observable.subscribe(item -> System.out.println("Sub-" + item));
    }


    @Test
    public void compositionFutureVsObservable() throws ExecutionException, InterruptedException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            sleepIgnore(10);
            return "FirstItem";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            sleepIgnore(15);
            return "SecondItem";
        });
        CompletableFuture<String> combineF1AndF2 = f1.thenCombine(f2, (x, y) -> x + "+" + y);
        String combined = combineF1AndF2.get();

        assertEquals("FirstItem+SecondItem", combined);

        Observable<String> o1 = Observable.create(s -> {
            sleepIgnore(10);
            s.onNext("FirstItem");
            s.onCompleted();
        });

        Observable<String> o2 = Observable.create(s -> {
            sleepIgnore(15);
            s.onNext("SecondItem");
            s.onCompleted();
        });

        Observable<String> combinedO1AndO2 = Observable.zip(o1, o2, (x, y) -> "Obs" + x + "+" + y);

        combinedO1AndO2.subscribe(System.out::println);

        Observable<String> mergedO1AndO2 = Observable.merge(o1, o2);
        mergedO1AndO2.subscribe(System.out::println);

        Thread.sleep(20);
    }


    private void sleepIgnore(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Single<String> getDataA() {
        return Single.<String>create(o -> {
            sleepIgnore(5);
            o.onSuccess("DataA");
        }).subscribeOn(Schedulers.io());
    }

    public Single<String> getDataB() {
        return Single.just("DataB").subscribeOn(Schedulers.io());
    }

    @Test
    public void singleTests() {

        Single<String> dataA = getDataA();
        Single<String> dataB = getDataB();

        dataA.subscribe(System.out::println);
        dataB.subscribe(System.out::println);

        Observable<String> merged = Single.merge(dataA, dataB);
        merged.subscribe(System.out::println);

    }

    public Completable writeToDatabase(Object data) {
        return Completable.create(s -> {
            doAsyncWrite(data, () -> s.onCompleted(), error -> s.onError(error));
        });
    }

    private void doAsyncWrite(Object data, Action0 onCompleted, Consumer<Throwable> onError) {
        try {
            sleepIgnore(100);
            if ("0" != data) {
                System.out.println("Write " + data + " to db");
                onCompleted.call();
            } else {
                throw new RuntimeException("Something unexpected happened.");
            }
        } catch (Exception ex) {
            onError.accept(ex);
        }
    }

    @Test
    public void writeToDatabaseTest1() {
        Completable completable = writeToDatabase("1");
        completable.subscribe(() -> {
            System.out.println("Subscriber Received Completed");
        });
    }

    @Test
    public void writeToDatabaseTest2() {
        Completable completable = writeToDatabase("0");
        completable.subscribe(() -> {
            System.out.println("Subscriber Received Completed");
        }, Throwable::printStackTrace);
    }
}

