package rxjava.examples.backpressuredb;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ResultSetIterator implements Iterator<Object[]> {

    private final ResultSet resultSet;

    public ResultSetIterator(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public boolean hasNext() {
        try {
            return !resultSet.isLast();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object[] next() {
        try {
            resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ArrayUtils.toArray(resultSet);
    }

    public static Iterable<Object[]> iterable(final ResultSet resultSet) {
        return () -> new ResultSetIterator(resultSet);
    }

}
