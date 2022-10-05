package rxjava.examples.backpressuredb;

import org.apache.commons.lang3.ArrayUtils;
import rx.Observable;
import rx.observables.SyncOnSubscribe;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetObservable {
    public static Observable<Object[]> create(ResultSet resultSet) {
        Observable.OnSubscribe<Object[]> onSubscribe = SyncOnSubscribe.createSingleState(
                () -> resultSet,
                (rs, observer) -> {
                    if (next(rs)) {
                        observer.onNext(ArrayUtils.toArray(rs));
                    } else {
                        observer.onCompleted();
                    }
                    observer.onNext(ArrayUtils.toArray(rs));
                },
                ResultSetObservable::close
        );
        return Observable.unsafeCreate(onSubscribe);
    }

    private static void close(ResultSet resultSet1) {
        try {
            resultSet1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static boolean next(ResultSet rs) {
        try {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
