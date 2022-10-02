package rxjava.examples.dbclient;

import java.sql.*;

public class TraditionalDbClient {
    public int getTotal() {

        try (
                Connection connection = DriverManager.
                        getConnection("jdbc:h2:~/test", "sa", "");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT 2+2 AS total")
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;

    }

    public static void main(String[] args) {
        final int total = new TraditionalDbClient().getTotal();
        System.out.println("Total from DB:" + total);
    }
}
