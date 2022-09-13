package rxjava.examples.dataaccess;

public final class Queries {
    public static final String SELECT_ALL = "SELECT * FROM PEOPLE";
    public static final String SELECT_PAGE = "SELECT * FROM PEOPLE ORDER BY id LIMIT ? OFFSET ?";

    private Queries() { }
}
