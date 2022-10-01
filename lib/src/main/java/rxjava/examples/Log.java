package rxjava.examples;

public class Log {

    private static final long start = System.currentTimeMillis();
    public static void log(Object label) {
        System.out.println(
                System.currentTimeMillis() - start + "\t| " +
                        Thread.currentThread().getName()   + "\t| " +
                        label);
    }
    public static void logWithName(Object message) {
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }
}
