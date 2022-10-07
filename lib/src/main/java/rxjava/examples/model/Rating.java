package rxjava.examples.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Rating {
    private Book book;
}
