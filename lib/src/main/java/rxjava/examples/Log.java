package rxjava.examples;

public class Log {

    public static void log(Object message) {
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }

    private static final long start = System.currentTimeMillis();

    public static void threadLog(Object label) {
        System.out.println(
                System.currentTimeMillis() - start + "\t| " +
                        Thread.currentThread().getName() + "\t|" +
                        label
        );
    }
}
