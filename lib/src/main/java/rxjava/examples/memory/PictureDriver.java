package rxjava.examples.memory;

import rx.Observable;
import rxjava.examples.Log;

import java.util.concurrent.TimeUnit;

public class PictureDriver {
    public static void main(String[] args) {
        distinctWithWindowSize();
    }

    private static void quickMemory() {
        Observable.range(0, Integer.MAX_VALUE)
                .map(Picture::new)
                .distinct()
                .sample(1, TimeUnit.SECONDS)
                .subscribe(Log::log);
    }

    private static void slowMemory() {
        Observable.range(0, Integer.MAX_VALUE)
                .map(Picture::new)
                .distinct(Picture::getTag)
                .sample(1, TimeUnit.SECONDS)
                .subscribe(Log::log);
    }

    private static void noIssue() {
        Observable.range(0, Integer.MAX_VALUE)
                .map(Picture::new)
                .window(1, TimeUnit.SECONDS)
                .flatMap(Observable::count)
                .subscribe(Log::log);
    }

    private static void distinctWithWindow() {
        Observable.range(0, Integer.MAX_VALUE)
                .map(Picture::new)
                .window(3, TimeUnit.SECONDS)
                .flatMap(Observable::distinct)
                .sample(1, TimeUnit.SECONDS)
                .subscribe(Log::log);
    }

    private static void distinctWithWindowSize() {
        Observable.range(0, Integer.MAX_VALUE)
                .map(Picture::new)
                .window(1000)
                .flatMap(Observable::distinct)
                .sample(1, TimeUnit.SECONDS)
                .subscribe(Log::log);
    }

}
