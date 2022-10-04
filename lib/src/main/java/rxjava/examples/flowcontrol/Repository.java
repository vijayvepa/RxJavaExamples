package rxjava.examples.flowcontrol;

import rxjava.examples.model.Book;

import java.util.List;

public interface Repository {
    void store(Book book);

    ;

    void storeAll(List<Book> bookList);
}
