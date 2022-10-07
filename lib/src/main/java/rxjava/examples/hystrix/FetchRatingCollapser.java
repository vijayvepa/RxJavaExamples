package rxjava.examples.hystrix;

import com.netflix.hystrix.*;
import rx.functions.Func1;
import rxjava.examples.model.Book;
import rxjava.examples.model.Rating;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FetchRatingCollapser extends HystrixObservableCollapser<Book, Rating, Rating, Book> {

    private final Book book;
    public FetchRatingCollapser(Book book) {
        super(
                Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("Books"))
                        .andCollapserPropertiesDefaults(
                                HystrixCollapserProperties.Setter()
                                        .withTimerDelayInMilliseconds(20)
                                        .withMaxRequestsInBatch(50))
                        .andScope(Scope.GLOBAL)
        );
        this.book = book;
        // requestContext = HystrixRequestContext.initializeContext();
    }

    @Override
    public Book getRequestArgument() {
        return book;
    }

    @Override
    protected HystrixObservableCommand<Rating> createCommand(
            Collection<HystrixCollapser.CollapsedRequest<Rating, Book>> collapsedRequests) {
        final List<Book> booksBatch =
                collapsedRequests.stream().map(HystrixCollapser.CollapsedRequest::getArgument).collect(Collectors.toList());
        return new FetchMyRatings(booksBatch);
    }

    @Override
    protected Func1<Rating, Book> getBatchReturnTypeKeySelector() {
        return Rating::getBook;
    }

    @Override
    protected Func1<Book, Book> getRequestArgumentKeySelector() {
        return x -> x;
    }

    @Override
    protected void onMissingResponse(HystrixCollapser.CollapsedRequest<Rating, Book> r) {

        r.setException(new RuntimeException("Not found for " + r.getArgument()));
    }

    @Override
    protected Func1<Rating, Rating> getBatchReturnTypeToResponseTypeMapper() {
        return x -> x;
    }
}
