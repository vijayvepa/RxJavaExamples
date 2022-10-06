package rxjava.examples.testingsupport;

import rx.Observable;

import java.time.LocalDate;

public interface MyService {
    Observable<LocalDate> externalCall();
}
