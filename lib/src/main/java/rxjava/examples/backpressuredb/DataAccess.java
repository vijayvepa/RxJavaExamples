package rxjava.examples.backpressuredb;

import rx.Observable;
import rxjava.examples.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataAccess {
    private final Connection connection;

    public DataAccess(Connection connection) {
        this.connection = connection;
    }

    public Observable<Object[]> getObservableResults(String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setFetchSize(1000);
        final ResultSet resultSet = statement.executeQuery();

        return Observable.from(ResultSetIterator.iterable(resultSet))
                .doAfterTerminate(() -> {
                    try {
                        resultSet.close();
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        Log.log("WARN: Unable to close:" + ex);
                    }
                });
    }
}
