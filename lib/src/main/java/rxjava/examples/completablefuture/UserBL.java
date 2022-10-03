package rxjava.examples.completablefuture;

import rx.Observable;
import rxjava.examples.ObservableUtils;
import rxjava.examples.model.User;

import java.util.concurrent.CompletableFuture;

public class UserBL {

    User findById(String id) {
        return new User();
    }

    CompletableFuture<User> findByIdAsync(String id) {
        return CompletableFuture.supplyAsync(() -> findById(id));
    }

    Observable<User> findByIdReactive(String id) {
        return ObservableUtils.fromCompletableFuture(findByIdAsync(id));
    }
}
