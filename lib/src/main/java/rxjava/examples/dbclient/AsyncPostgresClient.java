package rxjava.examples.dbclient;

import com.github.pgasync.impl.PgConnection;
import rx.Observable;
import rxjava.examples.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AsyncPostgresClient {

    void listen() {

        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:db");

                Statement statement = connection.createStatement()

        ) {
            PgConnection pgConn = (PgConnection) connection;
            final Observable<String> my_channel = pgConn.listen("my_channel");

            pollForNotifications(my_channel);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void pollForNotifications(Observable<String> my_channel) {
        my_channel.subscribe(Log::log);
    }

}
