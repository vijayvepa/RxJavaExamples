package rxjava.examples;

import java.util.concurrent.Callable;

public class TimeIt {

    public static <T> T printTime(Callable<T> task) {
        T call = null;
        try {
            long startTime = System.currentTimeMillis();
            call = task.call();
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.print( "Took "  + timeTaken / 1000d + "s");
        } catch (Exception e) {
            //...
        }
        return call;
    }
}
